import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.Vector2
import com.github.ityeri.graph.database.GraphRepository
import kotlinx.coroutines.*
import physical.PhysicalGraph
import java.sql.Connection
import java.sql.DriverManager

/** [com.badlogic.gdx.ApplicationListener] implementation shared by all platforms. */

class Viewer : ViewerAdapter() {

    lateinit var physicalGraph: PhysicalGraph
    lateinit var graphRepository: GraphRepository

    var dt: Float = 1 / 60f
    val randomGenerateRange: Float = 1000f

    val scope = CoroutineScope(Dispatchers.Default)

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
        world.step(1 / 30f, 32, 1)
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

        scope.launch {
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

        val edgeJobs = physicalGraph.edges.chunked(128).map { edgeChunk ->
            scope.launch {
                for (edge in edgeChunk) {

                    shapeRenderer.rectLine(
                        edge.startNode.x, edge.startNode.y,
                        edge.endNode.x, edge.endNode.y, 0.1f
                    )

                }
            }
        }

        edgeJobs.forEach { runBlocking { it.join() } }

        val nodeJobs = physicalGraph.nodes.chunked(128).map { nodeChunk ->
            scope.launch {
                for (node in nodeChunk) {

                    shapeRenderer.circle(node.x, node.y, node.displayRadius)ㄹㅇ

                }
            }
        }

        nodeJobs.forEach { runBlocking { it.join() } }
    }

    fun getMouseHoveringNode() {
        val mouseScreen = Vector2(Gdx.input.x.toFloat(), Gdx.input.y.toFloat())
        val mouseWorld = viewport.unproject(mouseScreen.cpy())

        shapeRenderer.color = Color(1f, 0f, 0f, 1f)
        shapeRenderer.circle(mouseWorld.x, mouseWorld.y, 1f)

    }

}
