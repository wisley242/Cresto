package com.nevoit.cresto.repository

import com.nevoit.cresto.data.TodoDao
import com.nevoit.cresto.data.TodoItem
import kotlinx.coroutines.flow.Flow

/**
 * A repository that provides a single source of truth for all to-do data.
 * It abstracts the data source (in this case, a Room database) from the rest of the app.
 *
 * @param todoDao The Data Access Object for the to-do items.
 */
class TodoRepository(private val todoDao: TodoDao) {

    /**
     * A flow that emits a list of all to-do items from the database.
     */
    val allTodos: Flow<List<TodoItem>> = todoDao.getAllTodos()

    /**
     * Inserts a new to-do item into the database.
     *
     * @param item The to-do item to insert.
     */
    suspend fun insert(item: TodoItem) {
        todoDao.insertTodo(item)
    }

    /**
     * Inserts a list of to-do items into the database.
     *
     * @param items The list of to-do items to insert.
     */
    suspend fun insertAll(items: List<TodoItem>) {
        todoDao.insertAll(items)
    }

    /**
     * Updates an existing to-do item in the database.
     *
     * @param item The to-do item to update.
     */
    suspend fun update(item: TodoItem) {
        todoDao.updateTodo(item)
    }

    /**
     * Deletes a to-do item from the database.
     *
     * @param item The to-do item to delete.
     */
    suspend fun delete(item: TodoItem) {
        todoDao.deleteTodo(item)
    }

}
