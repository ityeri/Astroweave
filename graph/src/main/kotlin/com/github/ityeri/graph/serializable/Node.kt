package com.github.ityeri.graph.serializable

import com.github.ityeri.graph.BaseNode


data class Node(
    val graph: Graph,
    override val name: String
): BaseNode {

    override fun getInDegrees(): Int {
        return graph.edges.count { it.endNode.name == this.name }
    }

    override fun getOutDegrees(): Int {
        return graph.edges.count { it.startNode.name == this.name }
    }

}
