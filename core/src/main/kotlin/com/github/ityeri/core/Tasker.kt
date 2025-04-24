package com.github.ityeri.core

import com.github.ityeri.graph.database.GraphRepository
import com.github.ityeri.graph.serializable.Edge
import com.github.ityeri.graph.serializable.Graph
import com.github.ityeri.graph.serializable.Node
import com.github.ityeri.utils.getLinks
import com.github.ityeri.utils.normalizeUrl
import com.github.ityeri.utils.toSha256
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.net.MalformedURLException
import java.sql.Connection

class Tasker(connection: Connection) {

    val graphRepository = GraphRepository(connection)
    val taskRepository = TaskRepository(connection)
    val graphStub = Graph()

    suspend fun runCycle(limit: Int) {
        val taskIds = taskRepository.popTaskIds(limit)

        for (id in taskIds) {

            val currentNode = graphRepository.getNodeById(id)!!
            val currentNodeStub = Node(graphStub, currentNode.name)

            val seedUrl = graphRepository.getNodeById(id)!!.name.normalizeUrl()
            val doc: Document

            try {
                HttpClient(CIO).use { client ->
                    val html = client.get(seedUrl).bodyAsText()
                    doc = Jsoup.parse(html)
                }
            }
            catch (_: Exception) { continue }

            for (url in doc.getLinks()) {
                val normalizedUrl: String
                try {
                    normalizedUrl = url.normalizeUrl()
                }
                catch (_: MalformedURLException) { continue }

                val newNodeStub = Node(graphStub, normalizedUrl)

                try {
                    graphRepository.addNode(newNodeStub)

                    graphRepository.addEdge(
                        Edge(graphStub, currentNodeStub, newNodeStub)
                    )

                    taskRepository.addTaskId(normalizedUrl.toSha256())
                }
                catch (_: IllegalArgumentException) {  }
            }
        }
    }

}
