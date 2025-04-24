package com.github.ityeri.core

import com.github.ityeri.graph.database.GraphRepository
import com.github.ityeri.graph.serializable.Edge
import com.github.ityeri.graph.serializable.Graph
import com.github.ityeri.graph.serializable.Node
import com.github.ityeri.utils.getLinks
import com.github.ityeri.utils.normalizeUrl
import com.github.ityeri.utils.toSha256
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.sql.Connection

class Tasker(connection: Connection) {

    val graphRepository = GraphRepository(connection)
    val taskRepository = TaskRepository(connection)
    val graphStub = Graph()

    fun runCycle(limit: Int) {
        val taskIds = taskRepository.popTaskIds(limit)

        for (id in taskIds) {

            val currentNode = graphRepository.getNodeById(id)!!
            val currentNodeStub = Node(graphStub, currentNode.name)

            val seedUrl = graphRepository.getNodeById(id)!!.name.normalizeUrl()
            val doc: Document

            try {
                doc = Jsoup.connect(seedUrl).get()
            }
            catch (_: Exception) { continue }

            doc.getLinks().forEach { url ->
                val normalizedUrl = url.normalizeUrl()

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
