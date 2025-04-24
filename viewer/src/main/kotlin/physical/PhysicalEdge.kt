package physical

import com.badlogic.gdx.physics.box2d.Joint
import com.badlogic.gdx.physics.box2d.joints.DistanceJointDef
import com.github.ityeri.graph.BaseEdge

data class PhysicalEdge(
    val graph: PhysicalGraph,
    override val startNode: PhysicalNode,
    override val endNode: PhysicalNode
) : BaseEdge {

    lateinit var joint: Joint

    fun createJoint() {

        val jointLength: Float = startNode.protectRadius + endNode.protectRadius

        val jointDef = DistanceJointDef().apply {
            bodyA = startNode.body
            bodyB = endNode.body
            localAnchorA.set(0f, 0f)
            localAnchorB.set(0f, 0f)
            length = jointLength
            frequencyHz = 1f
            dampingRatio = 0.5f
            collideConnected = true
        }

        joint = graph.world.createJoint(jointDef)
    }
}
