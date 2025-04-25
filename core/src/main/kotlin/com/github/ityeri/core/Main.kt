package com.github.ityeri.core

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import java.sql.Connection
import java.sql.DriverManager
import kotlinx.coroutines.*

fun main(): Unit = runBlocking {

    val taskerCount = 1000

    val jdbcUrl = "jdbc:mysql://localhost:3306/" +
        "astroweave_db" +
        "?useSSL=false" +
        "&serverTimezone=UTC" +
        "&allowPublicKeyRetrieval=true"

    val username = "root"
    val password = "root"

    val connection: Connection = DriverManager.getConnection(jdbcUrl, username, password)
    connection.autoCommit = false

    val httpClient = HttpClient(CIO)

    val taskerList = mutableListOf<Tasker>()
    val jobs = mutableListOf<Job>()

    for (i in 0 until taskerCount) {
        val tasker = Tasker(connection, httpClient)
        taskerList.add(tasker)
        println("새 작업 시작")

        jobs.add(
            launch {
                val taskerId = taskerList.indexOf(tasker)

                try {
                    while (true) {
                        println("$taskerId 번 작업자 주기 시작")
                        tasker.runCycle(5)
                        println("$taskerId 번 작업자 주기 끝")
                        delay(10)
                    }
                }
                catch (e: Exception) {
                    println("$taskerId 번 작업자가 다음과 같은 오류로 중단됨: ")
                    println(e)
                }
            }
        )
    }

    for (job in jobs) {
        job.join()
    }

    httpClient.close()
}
