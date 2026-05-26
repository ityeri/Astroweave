package com.github.ityeri.core.tools

import com.github.ityeri.graph.database.GraphRepository
import com.github.ityeri.graph.serializable.Graph
import java.io.File
import java.sql.Connection
import java.sql.DriverManager

fun main() {
    val jsonString = File("assets/node_data_large.json").readText()

    val jdbcUrl = "jdbc:mysql://localhost:3306/" +
        "astroweave_db" +
        "?useSSL=false" +
        "&serverTimezone=UTC" +
        "&allowPublicKeyRetrieval=true"

    val username = "root"
    val password = "root"

    val connection: Connection = DriverManager.getConnection(jdbcUrl, username, password)

    connection.prepareStatement("delete from edges").executeUpdate()
    connection.prepareStatement("delete from nodes").executeUpdate()

    val graph = Graph.decodeFromJson(jsonString)
    val graphRepository = GraphRepository(connection)

    graph.nodes.forEach {
        try { graphRepository.addNode(it) }
        catch (_: IllegalArgumentException) {}
    }
    graph.edges.forEach {
        try { graphRepository.addEdge(it) }
        catch (_: IllegalArgumentException) {}
    }

}
