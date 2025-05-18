package com.example.jetpack_compose_assignment_2.data.repository

import com.example.jetpack_compose_assignment_2.data.local.TodoDao
import com.example.jetpack_compose_assignment_2.data.model.Todo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TodoRepository @Inject constructor(
    private val todoDao: TodoDao
) {
    fun getActiveTodos(): Flow<List<Todo>> = 
        todoDao.getAllTodos().map { todos -> todos.filter { !it.completed } }

    fun getCompletedTodos(): Flow<List<Todo>> = 
        todoDao.getAllTodos().map { todos -> todos.filter { it.completed } }

    suspend fun refreshTodos() {
        try {
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
        val tempId = System.currentTimeMillis().toInt() * -1
        val newTodo = Todo(
            id = tempId,
            userId = 1,
            title = title,
            completed = false
        )
        todoDao.insertTodo(newTodo)
    }

    suspend fun updateTodoCompletion(id: Int, completed: Boolean) {
        todoDao.updateTodoCompletion(id, completed)
    }
} 