package com.github.ityeri.core

import java.sql.Connection

class TaskRepository(val connection: Connection) {

    fun addTaskId(id: String) {
        val sql = "insert into tasks (id) values (?)"

        connection.prepareStatement(sql).use { statement ->
            statement.setString(1, id)
            statement.executeUpdate()
        }

        connection.commit()
    }

    fun popTaskId(): String? {
        val id: String

        connection.prepareStatement(
            "select id from tasks order by id limit 1 for update"
        ).use { statement ->
            val resultSet = statement.executeQuery()
            if (!resultSet.next()) { return null }

            id = resultSet.getString("id")
        }

        connection.prepareStatement(
            "delete from tasks where id = ?"
        ).use { statement ->
            statement.setString(1, id)
            statement.executeUpdate()
        }

        connection.commit()

        return id
    }

    fun popTaskIds(limit: Int): List<String> {

        val taskIds = mutableListOf<String>()

        for (i in 0 until limit) {
            val taskId = popTaskId() ?: break

            taskIds.add(taskId)
        }

        return taskIds.toList()
    }
}
