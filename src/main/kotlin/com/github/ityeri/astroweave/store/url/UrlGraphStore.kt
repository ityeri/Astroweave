package com.github.ityeri.astroweave.store.url

import com.github.ityeri.astroweave.Edge
import io.mola.galimatias.URL

interface UrlGraphStore {
    fun add(url: URL)
    fun remove(url: URL): Boolean
    fun includes(url: URL): Boolean
    fun addEdge(edge: Edge)
    fun removeEdge(edge: Edge): Boolean
}
