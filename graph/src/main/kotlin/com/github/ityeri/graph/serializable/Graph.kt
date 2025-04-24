package com.github.ityeri.graph.serializable

import com.github.ityeri.graph.BaseEdge
import com.github.ityeri.graph.BaseGraph
import com.github.ityeri.graph.BaseNode
import kotlinx.serialization.json.Json



class Graph: BaseGraph {

    private val _nodes: MutableList<Node> = mutableListOf()
    private val _edges: MutableList<Edge> = mutableListOf()

    override val nodes: List<Node>
        get() = _nodes.toList()
    override val edges: List<Edge>
        get() = _edges.toList()



    companion object {
        fun decodeFromJson(string: String): Graph {
            val deserializedGraph = Json.decodeFromString<SerializableGraph>(string)

            val graph = Graph()

            graph._nodes.addAll(deserializedGraph.nodes.map { name -> Node(graph, name) })

            graph._edges.addAll(
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



    override fun getNode(name: String): Node {
        return nodes.first { it.name == name }
    }

    override fun addNode(node: BaseNode) {
        _nodes.add(Node(this, node.name))
    }

    override fun rmNode(node: BaseNode) {
        TODO("Not yet implemented")
    }

    override fun addEdge(edge: BaseEdge) {
        _edges.add(
            Edge(
                graph = this,
                startNode = getNode(edge.startNode.name),
                endNode = getNode(edge.startNode.name)
            )
        )
    }

    override fun rmEdge(edge: BaseEdge) {
        TODO("Not yet implemented")
    }
}
