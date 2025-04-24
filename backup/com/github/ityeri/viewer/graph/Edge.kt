package com.github.ityeri.viewer.graph

data class Edge(
    override val graph: Graph,
    override val startNode: Node,
    override val endNode: Node
): BaseEdge
