package com.github.ityeri.graph.database

import com.github.ityeri.graph.BaseEdge

data class RemoteEdge(
    override val startNode: RemoteNode,
    override val endNode: RemoteNode
): BaseEdge
