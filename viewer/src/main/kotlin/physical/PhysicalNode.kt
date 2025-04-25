package physical

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.CircleShape
import com.badlogic.gdx.physics.box2d.FixtureDef
import com.github.ityeri.graph.BaseNode
import kotlin.math.pow

data class PhysicalNode(
    val graph: PhysicalGraph,
    override val name: String
): BaseNode {

    lateinit var body: Body

    val x: Float
        get() = body.position.x
    val y: Float
        get() = body.position.y

    val physicalRadius: Float
        get() = (getInDegrees() + getOutDegrees()) * 0.01f + graph.minNodeDist

    val displayRadius: Float
        get() = physicalRadius


    val protectRadius: Float
        get() = getInDegrees() + getOutDegrees().toFloat() * 0.5f

//    var physicalRadius: Float
//        get() = body.fixtureList.firstOrNull()!!.shape
//            ?.takeIf { it is CircleShape }
//            ?.let { (it as CircleShape).radius }!!
//
//        set(value) {
//            val oldFixtures = body.fixtureList.toList()
//            for (fixture in oldFixtures) {
//                body.destroyFixture(fixture)
//            }
//
//            val shape = CircleShape().apply {
//                radius = value
//            }
//
//            val fixtureDef = FixtureDef().apply {
//                this.shape = shape
//                density = 1f
//                restitution = 0.8f
//            }
//
//            body.createFixture(fixtureDef)
//
//            shape.dispose()
//        }

    fun createBody(position: Vector2) {

        // BodyDef: 물체의 가장 기본 정의
        val bodyDef = BodyDef().apply {
            type = BodyDef.BodyType.DynamicBody
            this.position.set(position)
        }

        // Body: 실제 주 물체 클래스 (이 단계에서 body 는 이미 world 에 추가되있)
        body = graph.world.createBody(bodyDef)

        // Shape: 물체의 형태를 정의하는칭구
        val shape = CircleShape()
        shape.radius = physicalRadius

        // FixtureDef: 물제의 물리적 성질을 정의하는 칭구 (shape 포함)
        val fixtureDef = FixtureDef().apply {
            this.shape = shape
            density = 1f
            restitution = 0.8f
        }

        body.createFixture(fixtureDef)

        body.linearDamping = 0.02f
        body.angularDamping = 0.02f

        shape.dispose()
    }

    fun getInNodes(): List<PhysicalNode> {
        return graph.edges.filter { it.endNode == this }.map { it.startNode }
    }
    fun getOutNodes(): List<PhysicalNode> {
        return graph.edges.filter { it.startNode == this }.map { it.endNode }
    }

    override fun getInDegrees(): Int {
        return getInNodes().size
    }
    override fun getOutDegrees(): Int {
        return getOutNodes().size
    }
}
