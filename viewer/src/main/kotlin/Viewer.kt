import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.Vector2
import com.github.ityeri.graph.database.GraphRepository
import physical.PhysicalGraph
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.sql.Connection
import java.sql.DriverManager

/** [com.badlogic.gdx.ApplicationListener] implementation shared by all platforms. */

class Viewer : ViewerAdapter() {

    lateinit var physicalGraph: PhysicalGraph
    lateinit var graphRepository: GraphRepository

    var dt: Float = 1 / 60f
    val randomGenerateRange: Float = 1000f

    init {
        val jdbcUrl = "jdbc:mysql://localhost:3306/" +
            "astroweave_db" +
            "?useSSL=false" +
            "&serverTimezone=UTC" +
            "&allowPublicKeyRetrieval=true"

        val username = "root"
        val password = "root"

        val connection: Connection = DriverManager.getConnection(jdbcUrl, username, password)

        graphRepository = GraphRepository(connection)
    }

    fun compute() {
        world.step(1/60f, 32, 1)
    }

    override fun create() {
        super.create()

        physicalGraph = PhysicalGraph(world)

        graphRepository.nodes.forEach {
            try { physicalGraph.addNode(it) }
            catch (_: IllegalArgumentException) {}
        }
        graphRepository.edges.forEach {
            try { physicalGraph.addEdge(it) }
            catch (_: IllegalArgumentException) {}
        }

        physicalGraph.nodes.forEach { it.createBody(
            Vector2(
                Math.random().toFloat()*randomGenerateRange*2 - randomGenerateRange,
                Math.random().toFloat()*randomGenerateRange*2 - randomGenerateRange)
            )
        }

        physicalGraph.edges.forEach { it.createJoint() }

        println("total edge count: ${physicalGraph.edges.count()}")

        CoroutineScope(Dispatchers.Default).launch {
            delay(5000)
            while (true) {
                compute()
            }
        }
    }

    override fun logic(dt: Float) {
        this.dt = dt
        super.logic(dt)
    }

    override fun draw() {
        shapeRenderer.color = Color(1f, 1f, 1f, 0.2f)

        for (edge in physicalGraph.edges) {
            shapeRenderer.rectLine(
                edge.startNode.x, edge.startNode.y,
                edge.endNode.x, edge.endNode.y, 0.1f
            )
        }

        shapeRenderer.color = Color(1f, 1f, 1f, 0.5f)

        for (node in physicalGraph.nodes) {
            shapeRenderer.circle(node.x, node.y, node.displayRadius)
        }
    }

    fun getMouseHoveringNode() {
        val mouseScreen = Vector2(Gdx.input.x.toFloat(), Gdx.input.y.toFloat())
        val mouseWorld = viewport.unproject(mouseScreen.cpy())

        shapeRenderer.color = Color(1f, 0f, 0f, 1f)
        shapeRenderer.circle(mouseWorld.x, mouseWorld.y, 1f)

    }

}
