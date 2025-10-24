package com.nevoit.cresto

import android.app.Application
import com.nevoit.cresto.data.TodoDatabase
import com.nevoit.cresto.repository.TodoRepository
import com.tencent.mmkv.MMKV

class CrestoApplication : Application() {
    private val database by lazy { TodoDatabase.getDatabase(this) }
    val repository by lazy { TodoRepository(database.todoDao()) }

    override fun onCreate() {
        super.onCreate()
        MMKV.initialize(this)
    }
}