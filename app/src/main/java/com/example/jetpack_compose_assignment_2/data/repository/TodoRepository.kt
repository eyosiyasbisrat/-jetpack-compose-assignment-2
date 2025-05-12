package com.example.jetpack_compose_assignment_2.data.repository

import com.example.jetpack_compose_assignment_2.data.local.TodoDao
import com.example.jetpack_compose_assignment_2.data.model.Todo
import com.example.jetpack_compose_assignment_2.data.remote.TodoApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.emitAll
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TodoRepository @Inject constructor(
    private val todoApi: TodoApi,
    private val todoDao: TodoDao
) {
    fun getTodos(): Flow<List<Todo>> = flow {
        // Emit cached data first
        emitAll(todoDao.getAllTodos())

        try {
            // Fetch fresh data from network
            val freshTodos = todoApi.getTodos()
            // Update cache
            todoDao.deleteAllTodos()
            todoDao.insertTodos(freshTodos)
        } catch (e: Exception) {
            // If network request fails, the cached data will still be emitted
            e.printStackTrace()
        }
    }

    suspend fun getTodoById(id: Int): Todo? {
        return todoDao.getTodoById(id)
    }
} 