package com.github.ityeri.core

import io.github.oshai.kotlinlogging.KotlinLogging
import io.klogging.Klogging
import io.mola.galimatias.URL
import kotlinx.coroutines.delay

class Crawler(val graphStore: URLGraphStore, val tickInterval: Float) : Klogging {
    val waitingURLs: MutableList<URL> = mutableListOf()
    var isRunning = false

    fun addWaitingUrl(url: URL) {
        waitingURLs.add(url)
    }

    suspend fun tick() {
        logger.info { "asdf" }
    }

    suspend fun run() {
        isRunning = true

        while (isRunning) {
            tick()
            delay((tickInterval * 1000).toLong())
        }
    }

    fun stop() {
        isRunning = false
    }
}
