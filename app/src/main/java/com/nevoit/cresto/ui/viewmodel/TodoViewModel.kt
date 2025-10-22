package com.nevoit.cresto.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.nevoit.cresto.data.EventItem
import com.nevoit.cresto.data.TodoItem
import com.nevoit.cresto.repository.TodoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class TodoViewModel(private val repository: TodoRepository) : ViewModel() {
    // item basics
    val allTodos: StateFlow<List<TodoItem>> = repository.allTodos.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun insert(item: TodoItem) = viewModelScope.launch {
        repository.insert(item)
    }

    fun update(item: TodoItem) = viewModelScope.launch {
        repository.update(item)
    }

    fun delete(item: TodoItem) = viewModelScope.launch {
        repository.delete(item)
        _revealedItemId.value = null
    }


    // Swipe to delete
    private val _revealedItemId = MutableStateFlow<Int?>(null)

    val revealedItemId: StateFlow<Int?> = _revealedItemId

    fun onItemExpanded(itemId: Int) {
        if (_revealedItemId.value == itemId) return
        viewModelScope.launch {
            _revealedItemId.value = itemId
        }
    }

    fun onItemCollapsed(itemId: Int) {
        if (_revealedItemId.value != itemId) return
        viewModelScope.launch {
            _revealedItemId.value = null
        }
    }

    fun collapseRevealedItem() {
        if (_revealedItemId.value == null) return
        viewModelScope.launch {
            _revealedItemId.value = null
        }
    }

    fun insertAiGeneratedTodos(aiItems: List<EventItem>) {
        viewModelScope.launch {
            try {
                val todoItemsToInsert = aiItems.map { eventItem ->
                    TodoItem(
                        title = eventItem.title,
                        dueDate = LocalDate.parse(eventItem.date, DateTimeFormatter.ISO_LOCAL_DATE)
                    )
                }

                if (todoItemsToInsert.isNotEmpty()) {
                    repository.insertAll(todoItemsToInsert)
                }

            } catch (e: Exception) {
                println("Error inserting AI-generated todos: ${e.message}")
            }
        }
    }
}

class TodoViewModelFactory(private val repository: TodoRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TodoViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TodoViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}