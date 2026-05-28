package com.github.ityeri.astroweave

import com.github.ityeri.astroweave.store.url.UrlGraphStore
import com.github.ityeri.astroweave.util.extractAllAttrs
import com.github.ityeri.astroweave.util.isValidUrl
import io.klogging.Klogging
import io.ktor.client.*
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.request.*
import io.ktor.client.statement.bodyAsText
import io.ktor.client.statement.request
import io.ktor.http.isSuccess
import io.mola.galimatias.GalimatiasParseException
import io.mola.galimatias.URL
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.selects.onTimeout
import kotlinx.coroutines.selects.select
import org.jsoup.Jsoup
import kotlin.math.max

class Crawler(
    val graphStore: UrlGraphStore,
    val client: HttpClient,
    val scope: CoroutineScope,
    val workerCount: Int = 1,
    val sleepInterval: Float = 0.1f,
    val workerBatch: Int = 32
) : Klogging {
    val taskChannel: Channel<UrlTask> = Channel(capacity = 100_000)
    val newTaskChannel: Channel<UrlTask> = Channel(capacity = 100_000)
    var isRunning = false

    suspend fun addUrlTask(urlTask: UrlTask) {
        taskChannel.send(urlTask)
    }

    suspend fun run() {
        isRunning = true

        val workers = List(workerCount) { workerId ->
            scope.launch {
                logger.info { "Worker $workerId started" }
                while (true) {
                    val queuedTasks = taskChannel.receiveTasks(workerBatch, timeout = 3f)

                    val executingTasks = coroutineScope {
                        queuedTasks.map { urlTask ->
                            async {
                                try {
                                    val response = client.get(urlTask.to.toString())

                                    if (response.status.isSuccess()) {
                                        val finalUrl = URL.parse(response.request.url.toString())

                                        graphStore.add(finalUrl)

                                        if (urlTask.from != null) {
                                            graphStore.addEdge(Edge(urlTask.from, finalUrl))
                                        }
                                    }

                                    response
                                } catch (e: HttpRequestTimeoutException) {
                                    logger.warn { "Request timeout for url task: $urlTask. ignore" }
                                    null
                                } catch (e: Exception) {
                                    logger.error(e) {
                                        "Unknown error occurred while processing url task $urlTask: " +
                                            "${e.message}"
                                    }
                                    null
                                }
                            }
                        }
                    }

                    val result = executingTasks.awaitAll()

                    val newTasks = result.filterNotNull().flatMap { response ->
                        val originUrl = URL.parse(response.request.url.toString())
                        val document = Jsoup.parse(response.bodyAsText())

                        val foundUrls = document.extractAllAttrs()
                            .filter { it.isValidUrl(originUrl) }
                            .map { URL.parse(originUrl, it) }

                        foundUrls.map { UrlTask(originUrl, it) }
                    }

                    newTasks.forEach { task ->
                        newTaskChannel.send(task)
                    }

                    logger.info { "Worker $workerId ticking done" }

                    delay((sleepInterval * 1000).toLong())
                }
            }
        }

        val channelCoordinatingTask = scope.launch {
            for (urlTask in newTaskChannel) {
                taskChannel.send(urlTask)
            }
        }

        workers.joinAll()
        channelCoordinatingTask.join()
    }

    fun stop() {
        isRunning = false
    }
}

suspend fun <E> ReceiveChannel<E>.receiveTasks(n: Int, timeout: Float = -1f): List<E> {
    val batch = ArrayList<E>(n)

    val hasTimeout = timeout > 0f
    val timeoutMillis = if (hasTimeout) (timeout * 1000).toLong() else -1L
    val endTime = if (hasTimeout) System.currentTimeMillis() + timeoutMillis else -1L

    batch.add(this.receive())

    while (batch.size < n) {
        val remainingTime = if (hasTimeout) endTime - System.currentTimeMillis() else -1L

        if (hasTimeout && remainingTime <= 0) break

        val isChannelClosed = select {
            this@receiveTasks.onReceiveCatching { result ->
                if (result.isSuccess) {
                    batch.add(result.getOrThrow())
                    false
                } else {
                    true
                }
            }

            if (hasTimeout) {
                onTimeout(max(0, remainingTime)) {
                    false
                }
            }
        }

        if (isChannelClosed) break
    }

    return batch
}
