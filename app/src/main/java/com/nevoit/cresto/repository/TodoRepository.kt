package com.nevoit.cresto.repository

import com.nevoit.cresto.data.TodoDao
import com.nevoit.cresto.data.TodoItem
import kotlinx.coroutines.flow.Flow

class TodoRepository(private val todoDao: TodoDao) {

    // 直接从 DAO 获取 Flow 数据流。
    // ViewModel 将会监听这个 Flow 的变化。
    val allTodos: Flow<List<TodoItem>> = todoDao.getAllTodos()

    // Repository 提供了简洁的 API 供 ViewModel 调用，
    // 它在内部调用 DAO 的挂起函数来执行数据库操作。
    suspend fun insert(item: TodoItem) {
        todoDao.insertTodo(item)
    }

    suspend fun update(item: TodoItem) {
        todoDao.updateTodo(item)
    }

    suspend fun delete(item: TodoItem) {
        todoDao.deleteTodo(item)
    }

}