package com.example.jetpack_compose_assignment_2.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.example.jetpack_compose_assignment_2.data.local.TodoDao
import com.example.jetpack_compose_assignment_2.data.model.Todo
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TodoRepository @Inject constructor(
    private val todoDao: TodoDao,
    @ApplicationContext private val context: Context
) {
    init {
        clearDbOnFirstLaunch()
    }

    private fun clearDbOnFirstLaunch() {
        val prefs: SharedPreferences = context.getSharedPreferences("todo_prefs", Context.MODE_PRIVATE)
        if (!prefs.getBoolean("db_cleared", false)) {
            runBlocking { todoDao.deleteAllTodos() }
            prefs.edit().putBoolean("db_cleared", true).apply()
        }
    }

    fun getTodos(): Flow<List<Todo>> = todoDao.getAllTodos()

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