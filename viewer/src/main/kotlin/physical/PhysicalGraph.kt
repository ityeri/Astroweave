package physical

import com.badlogic.gdx.physics.box2d.World
import com.github.ityeri.graph.BaseEdge
import com.github.ityeri.graph.BaseGraph
import com.github.ityeri.graph.BaseNode

class PhysicalGraph(
    val world: World,
    override val nodes: MutableList<PhysicalNode> = mutableListOf(),
    override val edges: MutableList<PhysicalEdge> = mutableListOf(),
    val minNodeDist: Float = 0.5f
) : BaseGraph {

    override fun getNode(name: String): PhysicalNode {
        return nodes.first { it.name == name }
    }

    override fun addNode(node: BaseNode) {
        val physicalNode = PhysicalNode(this, node.name)

        if (physicalNode in nodes) {
            throw IllegalArgumentException()
        }

        nodes.add(physicalNode)
    }

    override fun rmNode(node: BaseNode) {
        TODO("Not yet implemented")
    }

    override fun addEdge(edge: BaseEdge) {
        val physicalEdge = PhysicalEdge(this,
            getNode(edge.startNode.name), getNode(edge.endNode.name))

        if (physicalEdge in edges) {
            throw IllegalArgumentException()
        }

        edges.add(physicalEdge)
    }

    override fun rmEdge(edge: BaseEdge) {
        TODO("Not yet implemented")
    }

}
