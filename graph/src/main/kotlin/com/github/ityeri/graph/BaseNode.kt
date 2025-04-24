package com.github.ityeri.graph

interface BaseNode {
    val name: String

    fun getInDegrees(): Int
    fun getOutDegrees(): Int
}
