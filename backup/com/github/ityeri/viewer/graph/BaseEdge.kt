package com.github.ityeri.viewer.graph

interface BaseEdge {
    val graph: BaseGraph
    val startNode: BaseNode
    val endNode: BaseNode
}
