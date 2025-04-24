package com.github.ityeri.viewer.graph


data class Node(
    override val graph: Graph,
    override val id: String
): BaseNode {

    override fun getInDegrees(): Int {
        return graph.edges.count { it.endNode == this }
    }

    override fun getOutDegrees(): Int {
        return graph.edges.count { it.startNode == this }
    }

}
