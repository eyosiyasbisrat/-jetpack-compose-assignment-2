package com.example.jetpack_compose_assignment_2.data.repository

import com.example.jetpack_compose_assignment_2.data.local.TodoDao
import com.example.jetpack_compose_assignment_2.data.model.Todo
import com.example.jetpack_compose_assignment_2.data.remote.TodoApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TodoRepository @Inject constructor(
    private val todoApi: TodoApi,
    private val todoDao: TodoDao
) {
    fun getTodos(): Flow<List<Todo>> = todoDao.getAllTodos()

    suspend fun refreshTodos() {
        try {
            // Instead of fetching from API, we'll just remove completed todos
            val todos = todoDao.getAllTodos().first()
            val incompleteTodos = todos.filter { !it.completed }
            todoDao.deleteAllTodos()
            todoDao.insertTodos(incompleteTodos)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun getTodoById(id: Int): Todo? {
        return todoDao.getTodoById(id)
    }

    suspend fun addTodo(title: String) {
        // Generate a temporary ID (negative to avoid conflicts with API IDs)
        val tempId = System.currentTimeMillis().toInt() * -1
        val newTodo = Todo(
            id = tempId,
            userId = 1, // Default user ID
            title = title,
            completed = false
        )
        todoDao.insertTodo(newTodo)
    }

    suspend fun updateTodoCompletion(id: Int, completed: Boolean) {
        todoDao.updateTodoCompletion(id, completed)
    }
} 