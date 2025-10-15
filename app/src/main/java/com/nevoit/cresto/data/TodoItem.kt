package com.nevoit.cresto.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "todo_items")
data class TodoItem(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val dueDate: Long? = null,
    val isCompleted: Boolean = false,
    val hashtag: String? = null,
    val flag: Int = 0
)