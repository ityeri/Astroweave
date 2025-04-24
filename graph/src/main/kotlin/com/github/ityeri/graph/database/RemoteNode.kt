package com.github.ityeri.graph.database

import com.github.ityeri.graph.BaseNode
import java.sql.Connection


data class RemoteNode(
    val connection: Connection,
    val hashId: String,
): BaseNode {

    override var name: String = ""

    init {
        val sql = "SELECT name FROM nodes WHERE id = ? limit 1"

        connection.prepareStatement(sql).use { statement ->
            statement.setString(1, hashId)
            val resultSet = statement.executeQuery()
            resultSet.next()

            name = resultSet.getString("name")
        }
    }

    override fun getInDegrees(): Int {
        val sql = "select count(*) from edges where end_node_id = ?"

        connection.prepareStatement(sql).use { statement ->
            statement.setString(1, hashId)

            val resultSet = statement.executeQuery()
            resultSet.next()

            return resultSet.getInt(1)
        }
    }

    override fun getOutDegrees(): Int {
        val sql = "select count(*) from edges where start_node_id = ?"

        connection.prepareStatement(sql).use { statement ->
            statement.setString(1, hashId)

            val resultSet = statement.executeQuery()
            resultSet.next()

            return resultSet.getInt(1)
        }
    }

}
