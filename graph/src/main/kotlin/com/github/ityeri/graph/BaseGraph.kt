package com.github.ityeri.graph

interface BaseGraph {

    val nodes: List<BaseNode>
    val edges: List<BaseEdge>

    fun getNode(name: String): BaseNode?

    fun addNode(node: BaseNode)
    fun rmNode(node: BaseNode)

    fun addEdge(edge: BaseEdge)
    fun rmEdge(edge: BaseEdge)
}
