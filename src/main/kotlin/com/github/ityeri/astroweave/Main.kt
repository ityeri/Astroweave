package com.github.ityeri.astroweave

import io.ktor.client.HttpClient
import io.mola.galimatias.URL
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

        val koin = KoinPlatform.getKoin()
        val crawler = koin.get<Crawler>()
        crawler.addUrlTask(
            UrlTask(null, URL.parse("https://google.com"))
        )

        try {
            crawler.run()
        } finally {
            koin.get<HttpClient>().close()
        }
    }
}
