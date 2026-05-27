package com.github.ityeri.core

import kotlinx.coroutines.runBlocking
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.logger.SLF4JLogger
import org.koin.mp.KoinPlatform

fun main() {
    println("main: Starting...")

    startKoin {
        logger(SLF4JLogger(Level.INFO))
        modules(appModule)
    }

    runBlocking {
        println("main: Coroutine started")
        val crawler = KoinPlatform.getKoin().get<Crawler>()
        crawler.run()
    }
}
