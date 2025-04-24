package com.github.ityeri.viewer.graph.physical

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.World
import com.github.ityeri.viewer.graph.BaseEdge
import com.github.ityeri.viewer.graph.BaseGraph
import com.github.ityeri.viewer.graph.BaseNode

class PhysicalGraph(
    val world: World,
    override val nodes: MutableList<PhysicalNode> = mutableListOf(),
    override val edges: MutableList<PhysicalEdge> = mutableListOf(),
    val minNodeDist: Float = 1f
) : BaseGraph {

    fun addNode(node: BaseNode) {
        val physicalNode = PhysicalNode(this, node.id)

        if (physicalNode in nodes) {
            throw IllegalArgumentException()
        }

        nodes.add(physicalNode)
    }

    fun addEdge(edge: BaseEdge) {
        val physicalEdge = PhysicalEdge(this,
            getNode(edge.startNode.id), getNode(edge.endNode.id))

        if (physicalEdge in edges) {
            throw IllegalArgumentException()
        }

        edges.add(physicalEdge)
    }

    fun getNode(id: String): PhysicalNode {
        return nodes.first { it.id == id }
    }

}
