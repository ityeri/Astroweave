package com.github.ityeri.viewer

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.Vector2
import com.github.ityeri.viewer.graph.Graph
import com.github.ityeri.viewer.graph.physical.PhysicalGraph
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File

/** [com.badlogic.gdx.ApplicationListener] implementation shared by all platforms. */

class Viewer : ViewerAdapter() {

    lateinit var physicalGraph: PhysicalGraph
    var dt: Float = 1 / 60f
    val randomGenerateRange: Float = 1000f

    fun compute() {
        world.step(dt, 32, 1)
    }

    override fun create() {
        super.create()

        physicalGraph = PhysicalGraph(world)

        val jsonString = File("assets/node_data_large.json").readText()
        val graph = Graph.decodeFromJson(jsonString)

        graph.nodes.forEach {
            try { physicalGraph.addNode(it) }
            catch (_: IllegalArgumentException) {}
        }
        graph.edges.forEach {
            try { physicalGraph.addEdge(it) }
            catch (_: IllegalArgumentException) {}
        }

        physicalGraph.nodes.forEach { it.createBody(
            Vector2(
                Math.random().toFloat()*randomGenerateRange*2 - randomGenerateRange,
                Math.random().toFloat()*randomGenerateRange*2 - randomGenerateRange)
        ) }

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


}
