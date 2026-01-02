package com.example.moviesdatabase.presentation.navigation

sealed class Screen(val route: String) {
    object Home: Screen("home")
    object Search: Screen("search")
    object Bookmarks: Screen("bookmarks")
    object Detail: Screen("detail/{movieId}") {
        fun createRoute(movieId: Int) = "detail/$movieId"
    }
}