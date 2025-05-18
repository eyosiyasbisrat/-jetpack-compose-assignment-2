package com.example.jetpack_compose_assignment_2.di

import android.content.Context
import com.example.jetpack_compose_assignment_2.data.local.TodoDatabase
import com.example.jetpack_compose_assignment_2.data.local.TodoDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideTodoDatabase(
        @ApplicationContext context: Context
    ): TodoDatabase {
        return TodoDatabase.getDatabase(context)
    }

    @Provides
    @Singleton
    fun provideTodoDao(database: TodoDatabase): TodoDao {
        return database.todoDao()
    }

    @Provides
    @Singleton
    fun provideAppContext(@ApplicationContext context: Context): Context = context
} 