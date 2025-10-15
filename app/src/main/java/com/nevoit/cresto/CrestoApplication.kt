package com.nevoit.cresto

import android.app.Application
import com.nevoit.cresto.data.TodoDatabase
import com.nevoit.cresto.repository.TodoRepository

class CrestoApplication: Application() {
    // 使用 lazy 委托，确保数据库和仓库只在第一次被访问时才创建
    private val database by lazy { TodoDatabase.getDatabase(this) }
    val repository by lazy { TodoRepository(database.todoDao()) }
}