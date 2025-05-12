package com.example.jetpack_compose_assignment_2.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.jetpack_compose_assignment_2.data.model.Todo
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoDao {
    @Query("SELECT * FROM todos")
    fun getAllTodos(): Flow<List<Todo>>

    @Query("SELECT * FROM todos WHERE id = :id")
    suspend fun getTodoById(id: Int): Todo?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTodos(todos: List<Todo>)

    @Query("DELETE FROM todos")
    suspend fun deleteAllTodos()
} 