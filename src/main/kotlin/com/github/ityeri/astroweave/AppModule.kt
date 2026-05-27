package com.github.ityeri.astroweave

import com.github.ityeri.astroweave.impl.memory.URLGraphStoreMemoryImpl
import org.koin.dsl.module

val appModule = module {
    single<URLGraphStore> {
        URLGraphStoreMemoryImpl()
    }
    single<Crawler> {
        Crawler(graphStore = get(), tickInterval = 1.0f)
    }
}
