package com.github.ityeri.core.tools

import com.github.ityeri.core.TaskRepository
import com.github.ityeri.graph.database.GraphRepository
import com.github.ityeri.graph.serializable.Graph
import com.github.ityeri.graph.serializable.Node
import com.github.ityeri.utils.normalizeUrl
import com.github.ityeri.utils.toSha256
import java.sql.Connection
import java.sql.DriverManager

fun main() {
    val seedLink = "https://namu.wiki".normalizeUrl()

    println("이 작업은 현재 모든 DB 를 초기화 합니다. 계속하시겠습니까? (Y/n)")
    if (readln() != "Y") {
        println("작업을 취소합니다")
        return
    }

    val jdbcUrl = "jdbc:mysql://localhost:3306/" +
        "astroweave_db" +
        "?useSSL=false" +
        "&serverTimezone=UTC" +
        "&allowPublicKeyRetrieval=true"

    val username = "root"
    val password = "root"

    val connection: Connection = DriverManager.getConnection(jdbcUrl, username, password)
    connection.autoCommit = false

    connection.prepareStatement("delete from tasks").executeUpdate()
    connection.prepareStatement("delete from edges").executeUpdate()
    connection.prepareStatement("delete from nodes").executeUpdate()

    val graphRepository = GraphRepository(connection)
    val taskRepository = TaskRepository(connection)

    graphRepository.addNode(
        Node(Graph(), seedLink)
    )
    taskRepository.addTaskId(
        seedLink.toSha256()
    )

    println("모든 태스크, 엣지, 노드를 제거하고 \"$seedLink\" 주소를 가진 종자 노드를 추가했습니다")

    connection.commit()
}
