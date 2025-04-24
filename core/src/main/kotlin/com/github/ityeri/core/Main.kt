package com.github.ityeri.core

import java.sql.Connection
import java.sql.DriverManager

fun main() {

    val jdbcUrl = "jdbc:mysql://localhost:3306/" +
        "astroweave_db" +
        "?useSSL=false" +
        "&serverTimezone=UTC" +
        "&allowPublicKeyRetrieval=true"

    val username = "root"
    val password = "root"

    val connection: Connection = DriverManager.getConnection(jdbcUrl, username, password)
    connection.autoCommit = false

    val tasker = Tasker(connection)
    tasker.runCycle(100)
}
