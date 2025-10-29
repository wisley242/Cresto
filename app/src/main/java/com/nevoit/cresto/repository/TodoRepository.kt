package com.nevoit.cresto.repository

import com.nevoit.cresto.data.TodoDao
import com.nevoit.cresto.data.TodoItem
import kotlinx.coroutines.flow.Flow

class TodoRepository(private val todoDao: TodoDao) {

    val allTodos: Flow<List<TodoItem>> = todoDao.getAllTodos()

    suspend fun insert(item: TodoItem) {
        todoDao.insertTodo(item)
    }

    suspend fun insertAll(items: List<TodoItem>) {
        todoDao.insertAll(items)
    }

    suspend fun update(item: TodoItem) {
        todoDao.updateTodo(item)
    }

    suspend fun delete(item: TodoItem) {
        todoDao.deleteTodo(item)
    }

}