package com.example.jetpack_compose_assignment_2.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jetpack_compose_assignment_2.data.model.Todo
import com.example.jetpack_compose_assignment_2.data.repository.TodoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TodoListViewModel @Inject constructor(
    private val repository: TodoRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<TodoListUiState>(TodoListUiState.Loading)
    val uiState: StateFlow<TodoListUiState> = _uiState

    init {
        loadTodos()
    }

    private fun loadTodos() {
        viewModelScope.launch {
            repository.getTodos()
                .catch { e ->
                    _uiState.value = TodoListUiState.Error(e.message ?: "Unknown error occurred")
                }
                .collectLatest { todos ->
                    if (todos.isEmpty()) {
                        _uiState.value = TodoListUiState.Empty
                    } else {
                        _uiState.value = TodoListUiState.Success(todos)
                    }
                }
        }
    }

    fun refreshTodos() {
        _uiState.value = TodoListUiState.Loading
        viewModelScope.launch {
            try {
                repository.refreshTodos()
            } catch (e: Exception) {
                // If DB is empty, error will be shown by loadTodos
                // If DB has data, user will see cached data
            }
        }
    }

    fun addTodo(title: String) {
        viewModelScope.launch {
            try {
                repository.addTodo(title)
            } catch (e: Exception) {
                _uiState.value = TodoListUiState.Error("Failed to add todo: ${e.message}")
            }
        }
    }

    fun updateTodoCompletion(id: Int, completed: Boolean) {
        viewModelScope.launch {
            try {
                repository.updateTodoCompletion(id, completed)
            } catch (e: Exception) {
                _uiState.value = TodoListUiState.Error("Failed to update todo: ${e.message}")
            }
        }
    }
}

sealed class TodoListUiState {
    data object Loading : TodoListUiState()
    data object Empty : TodoListUiState()
    data class Success(val todos: List<Todo>) : TodoListUiState()
    data class Error(val message: String) : TodoListUiState()
} 