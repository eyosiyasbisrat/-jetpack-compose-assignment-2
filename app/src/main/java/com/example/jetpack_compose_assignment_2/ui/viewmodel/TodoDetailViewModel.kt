package com.example.jetpack_compose_assignment_2.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jetpack_compose_assignment_2.data.model.Todo
import com.example.jetpack_compose_assignment_2.data.repository.TodoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TodoDetailViewModel @Inject constructor(
    private val repository: TodoRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow<TodoDetailUiState>(TodoDetailUiState.Loading)
    val uiState: StateFlow<TodoDetailUiState> = _uiState

    init {
        val todoId = savedStateHandle.get<Int>("todoId")
        if (todoId != null) {
            loadTodo(todoId)
        } else {
            _uiState.value = TodoDetailUiState.Error("Todo ID not found")
        }
    }

    private fun loadTodo(id: Int) {
        viewModelScope.launch {
            try {
                val todo = repository.getTodoById(id)
                if (todo != null) {
                    _uiState.value = TodoDetailUiState.Success(todo)
                } else {
                    _uiState.value = TodoDetailUiState.Error("Todo not found")
                }
            } catch (e: Exception) {
                _uiState.value = TodoDetailUiState.Error(e.message ?: "Unknown error occurred")
            }
        }
    }
}

sealed class TodoDetailUiState {
    data object Loading : TodoDetailUiState()
    data class Success(val todo: Todo) : TodoDetailUiState()
    data class Error(val message: String) : TodoDetailUiState()
} 