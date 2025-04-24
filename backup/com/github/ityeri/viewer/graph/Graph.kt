package com.github.ityeri.viewer.graph

import kotlinx.serialization.json.Json


class Graph(
    override val nodes: MutableList<Node> = mutableListOf(),
    override val edges: MutableList<Edge> = mutableListOf()
): BaseGraph {

    companion object {
        fun decodeFromJson(string: String): Graph {
            val deserializedGraph = Json.decodeFromString<SerializableGraph>(string)

            val graph = Graph(mutableListOf(), mutableListOf())

            graph.nodes.addAll(
                deserializedGraph.nodes.map { Node(graph, it) }
            )

            graph.edges.addAll(
                deserializedGraph.edges.map {
                    Edge(
                        graph = graph,
                        startNode = graph.getNode(it[0]),
                        endNode = graph.getNode(it[1])
                    )
                }
            )

            return graph

        }
    }

    fun getNode(id: String): Node {
        return nodes.first { it.id == id }
    }
}
