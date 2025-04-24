package com.github.ityeri.graph.database

import com.github.ityeri.graph.BaseEdge
import com.github.ityeri.graph.BaseGraph
import com.github.ityeri.graph.BaseNode
import com.github.ityeri.utils.toSha256
import java.sql.Connection
import java.sql.SQLIntegrityConstraintViolationException


class GraphRepository(val connection: Connection): BaseGraph {

    override val nodes: List<RemoteNode>
        get() {
            val nodes = mutableListOf<RemoteNode>()

            val sql = "SELECT * FROM nodes"

            connection.prepareStatement(sql).use { statement ->
                val resultSet = statement.executeQuery()

                while (resultSet.next()) {
                    nodes.add(RemoteNode(connection, resultSet.getString("id")))
                }
            }

            return nodes.toList()

        }

    override val edges: List<RemoteEdge>
        get() {
            val edges = mutableListOf<RemoteEdge>()

            val sql = "SELECT * FROM edges"

            connection.prepareStatement(sql).use { statement ->
                val resultSet = statement.executeQuery()


                while (resultSet.next()) {
                    edges.add(RemoteEdge(
                        getNodeById(resultSet.getString("start_node_id"))!!,
                        getNodeById(resultSet.getString("end_node_id"))!!
                    ))
                }
            }

            return edges.toList()
        }



    override fun getNode(name: String): RemoteNode? {
        val sql = "SELECT * FROM nodes WHERE name = ? limit 1"

        connection.prepareStatement(sql).use { statement ->
            statement.setString(1, name)  // 첫 번째 물음표(?)에 id를 바인딩
            val resultSet = statement.executeQuery()

            return if (resultSet.next()) {
                RemoteNode(connection, resultSet.getString("id"))
            } else {
                null
            }
        }
    }

    fun getNodeById(id: String): RemoteNode? {
        val sql = "SELECT * FROM nodes WHERE id = ? limit 1"

        connection.prepareStatement(sql).use { statement ->
            statement.setString(1, id)  // 첫 번째 물음표(?)에 id를 바인딩
            val resultSet = statement.executeQuery()

            return if (resultSet.next()) {
                RemoteNode(connection, resultSet.getString("id"))
            } else {
                null
            }
        }
    }



    override fun addNode(node: BaseNode) {
        val sql = "insert into nodes (id, name) values (?, ?)"

        connection.prepareStatement(sql).use { statement ->
            statement.setString(1, node.name.toSha256())
            statement.setString(2, node.name)

            try {
                statement.executeUpdate()
            }
            catch (_: SQLIntegrityConstraintViolationException) {
                connection.rollback()
                throw IllegalArgumentException("해당 노드는 중복됩니다")
            }
        }

        connection.commit()
    }

    override fun rmNode(node: BaseNode) {
        TODO("Not yet implemented")
    }

    override fun addEdge(edge: BaseEdge) {
        val sql = "insert into edges (start_node_id, end_node_id) values (?, ?)"

        connection.prepareStatement(sql).use { statement ->
            statement.setString(1, edge.startNode.name.toSha256())
            statement.setString(2, edge.endNode.name.toSha256())

            try {
                statement.executeUpdate()
            }
            catch (_: SQLIntegrityConstraintViolationException) {
                connection.rollback()
                throw IllegalArgumentException("해당 엣지는 중복됩니다")
            }
        }

        connection.commit()
    }

    override fun rmEdge(edge: BaseEdge) {
        TODO("Not yet implemented")
    }

}
