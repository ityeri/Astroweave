package com.github.ityeri.viewer

import com.github.ityeri.viewer.graph.Graph
import java.io.File

fun main () {

    val jsonString = File("assets/node_data.json").readText()

    val graph = Graph.decodeFromJson(jsonString)

    graph
}
