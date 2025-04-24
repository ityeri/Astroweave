package com.github.ityeri.graph.serializable

import kotlinx.serialization.Serializable

@Serializable
data class SerializableGraph (
    val nodes: List<String>,
    val edges: List<List<String>>
)
