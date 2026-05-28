package com.github.ityeri.astroweave.store.url.impl.memory

import com.github.ityeri.astroweave.Edge
import com.github.ityeri.astroweave.store.url.UrlGraphStore
import com.github.ityeri.astroweave.store.url.BadEdgeUrlException
import io.mola.galimatias.URL

class UrlGraphStoreMemoryImpl : UrlGraphStore {
    val urls: MutableSet<URL> = hashSetOf()
    val edges: MutableSet<Edge> = hashSetOf()

    override fun add(url: URL) {
        urls.add(url)
    }
    override fun remove(url: URL): Boolean {
        return urls.remove(url)
    }
    override fun includes(url: URL): Boolean {
        return urls.contains(url)
    }
    override fun addEdge(edge: Edge) {
        if (!urls.contains(edge.from) || !urls.contains(edge.to)) {
            throw BadEdgeUrlException()
        }
        edges.add(edge)
    }
    override fun removeEdge(edge: Edge): Boolean {
        return edges.remove(edge)
    }
}
