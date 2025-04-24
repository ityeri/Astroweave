package com.github.ityeri.viewer.graph

interface BaseNode {
    val graph: BaseGraph
    val id: String

    fun getInDegrees(): Int
    fun getOutDegrees(): Int
}
