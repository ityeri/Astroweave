package com.github.ityeri.graph.serializable

import com.github.ityeri.graph.BaseEdge

data class Edge(
    val graph: Graph,
    override val startNode: Node,
    override val endNode: Node
): BaseEdge
