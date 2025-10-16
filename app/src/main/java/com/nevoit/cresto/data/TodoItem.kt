package com.nevoit.cresto.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "todo_items")
data class TodoItem(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val dueDate: LocalDate? = null,
    val isCompleted: Boolean = false,
    val hashtag: String? = null,
    val flag: Int = 0
)