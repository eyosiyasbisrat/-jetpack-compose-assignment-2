package com.example.jetpack_compose_assignment_2.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jetpack_compose_assignment_2.data.model.Todo
import com.example.jetpack_compose_assignment_2.data.repository.TodoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
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
                .collect { todos ->
                    _uiState.value = TodoListUiState.Success(todos)
                }
        }
    }

    fun refreshTodos() {
        loadTodos()
    }
}

sealed class TodoListUiState {
    data object Loading : TodoListUiState()
    data class Success(val todos: List<Todo>) : TodoListUiState()
    data class Error(val message: String) : TodoListUiState()
} 