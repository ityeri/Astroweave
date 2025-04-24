package com.github.ityeri.viewer.graph

import kotlinx.serialization.Serializable

@Serializable
data class SerializableGraph (
    val nodes: List<String>,
    val edges: List<List<String>>
)
