package com.github.ityeri.core

import java.sql.Connection
import java.sql.DriverManager
import kotlinx.coroutines.*

fun main() = runBlocking {

    val threadCount = 1000

    val jdbcUrl = "jdbc:mysql://localhost:3306/" +
        "astroweave_db" +
        "?useSSL=false" +
        "&serverTimezone=UTC" +
        "&allowPublicKeyRetrieval=true"

    val username = "root"
    val password = "root"

    val connection: Connection = DriverManager.getConnection(jdbcUrl, username, password)
    connection.autoCommit = false

    val taskerList = mutableListOf<Tasker>()

    for (i in 0 until threadCount) {
        val tasker = Tasker(connection)
        taskerList.add(tasker)

        launch {
            val taskerId = taskerList.indexOf(tasker)

            try {
                while (true) {
                    println("$taskerId 번 작업자 주기 시작")
                    tasker.runCycle(100)
                    println("$taskerId 번 작업자 주기 끝")
                }
            }
            catch (e: Exception) {
                println("$taskerId 번 작업자가 다음과 같은 오류로 중단됨: ")
                println(e)
            }
        }
    }

}
