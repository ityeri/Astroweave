package com.github.ityeri.astroweave

import com.github.ityeri.astroweave.store.url.impl.memory.UrlGraphStoreMemoryImpl
import com.github.ityeri.astroweave.store.url.UrlGraphStore
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.engine.cio.endpoint
import io.ktor.client.plugins.HttpTimeout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.koin.dsl.module

val appModule = module {
    single<UrlGraphStore> {
        UrlGraphStoreMemoryImpl()
    }
    single<HttpClient> {
        HttpClient(CIO) {
            engine {
                endpoint {
                    connectTimeout = Long.MAX_VALUE
                    socketTimeout = Long.MAX_VALUE
                }
            }
            install(HttpTimeout) {
                requestTimeoutMillis = 60_000
                connectTimeoutMillis = Long.MAX_VALUE
                socketTimeoutMillis = Long.MAX_VALUE
            }
        }
    }
    single<Crawler> {
        Crawler(
            graphStore = get(),
            client = get(),
            scope = CoroutineScope(Dispatchers.IO)
        )
    }
}
