package com.nevoit.cresto.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoDao {
    @Insert (onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTodo(item: TodoItem)

    @Update
    suspend fun updateTodo(item: TodoItem)

    @Delete
    suspend fun deleteTodo(item: TodoItem)

    @Query("SELECT * FROM todo_items ORDER BY id DESC")
    fun getAllTodos(): Flow<List<TodoItem>>

    @Query("SELECT * FROM todo_items ORDER BY dueDate IS NULL, dueDate ASC")
    fun getAllTodosSortedByDueDate(): Flow<List<TodoItem>>
}