package com.nevoit.cresto.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.nevoit.cresto.data.TodoItem
import com.nevoit.cresto.repository.TodoRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TodoViewModel(private val repository: TodoRepository) : ViewModel() {
    // 使用 stateIn 将 Repository 中的 Flow 转换为 StateFlow
    // StateFlow 是一个“热”数据流，它会持有最新的值，非常适合 UI 层使用
    val allTodos: StateFlow<List<TodoItem>> = repository.allTodos.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000), // 5秒后如果没有订阅者则停止
        initialValue = emptyList() // 初始值为空列表
    )

    // 插入操作，在 viewModelScope 协程中执行
    fun insert(item: TodoItem) = viewModelScope.launch {
        repository.insert(item)
    }

    // 更新操作
    fun update(item: TodoItem) = viewModelScope.launch {
        repository.update(item)
    }

    // 删除操作
    fun delete(item: TodoItem) = viewModelScope.launch {
        repository.delete(item)
    }
}

// ViewModel 工厂类，告诉系统如何创建我们的 TodoViewModel
class TodoViewModelFactory(private val repository: TodoRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TodoViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TodoViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}