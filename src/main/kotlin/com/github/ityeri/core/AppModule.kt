package com.github.ityeri.core

import com.github.ityeri.core.impl.memory.URLGraphStoreMemoryImpl
import org.koin.dsl.module

val appModule = module {
    single<URLGraphStore> {
        URLGraphStoreMemoryImpl()
    }
    single<Crawler> {
        Crawler(graphStore = get(), tickInterval = 0.1f)
    }
}
