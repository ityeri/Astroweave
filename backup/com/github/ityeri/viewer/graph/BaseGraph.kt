package com.github.ityeri.viewer.graph

interface BaseGraph {
    val nodes: List<BaseNode>
    val edges: List<BaseEdge>

//    fun addNode(node: BaseNode)
//    fun rmNode(node: BaseNode)
//
//    fun addEdge(edge: BaseEdge)
//    fun rmEdge(edge: BaseEdge)
}
