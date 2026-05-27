package com.github.ityeri.astroweave

import io.mola.galimatias.URL

interface URLGraphStore {
    fun add(url: URL)
    fun remove(url: URL): Boolean
    fun includes(url: URL): Boolean
    fun addEdge(edge: Edge)
    fun removeEdge(edge: Edge): Boolean
}
