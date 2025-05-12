package com.example.jetpack_compose_assignment_2.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.jetpack_compose_assignment_2.ui.screens.TodoDetailScreen
import com.example.jetpack_compose_assignment_2.ui.screens.TodoListScreen

sealed class Screen(val route: String) {
    data object TodoList : Screen("todoList")
    data object TodoDetail : Screen("todoDetail/{todoId}") {
        fun createRoute(todoId: Int) = "todoDetail/$todoId"
    }
}

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.TodoList.route
    ) {
        composable(Screen.TodoList.route) {
            TodoListScreen(
                onTodoClick = { todoId ->
                    navController.navigate(Screen.TodoDetail.createRoute(todoId))
                }
            )
        }

        composable(
            route = Screen.TodoDetail.route,
            arguments = listOf(
                navArgument("todoId") {
                    type = NavType.IntType
                }
            )
        ) {
            TodoDetailScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
} 