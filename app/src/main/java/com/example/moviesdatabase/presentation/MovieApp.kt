package com.example.moviesdatabase.presentation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navDeepLink
import com.example.moviesdatabase.presentation.bookmarks.BookmarksScreen
import com.example.moviesdatabase.presentation.detail.DetailScreen
import com.example.moviesdatabase.presentation.home.HomeScreen
import com.example.moviesdatabase.presentation.navigation.Screen
import com.example.moviesdatabase.presentation.search.SearchScreen

@Composable
fun MovieApp() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            NavigationBar{
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                NavigationBarItem(
                    icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                    label = { Text("Home") },
                    selected = currentRoute == Screen.Home.route,
                    onClick = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(navController.graph.findStartDestination().id)
                            launchSingleTop = true
                        }
                    }
                )

                NavigationBarItem(
                    icon = { Icon(Icons.Default.Search, contentDescription = "Search") },
                    label = { Text("Search") },
                    selected = currentRoute == Screen.Search.route,
                    onClick = {
                        navController.navigate(Screen.Search.route) {
                            popUpTo(navController.graph.findStartDestination().id)
                            launchSingleTop = true
                        }
                    }
                )

                NavigationBarItem(
                    icon = { Icon(Icons.Default.Favorite, contentDescription = "Saved") },
                    label = { Text("Saved") },
                    selected = currentRoute == Screen.Bookmarks.route,
                    onClick = {
                        navController.navigate(Screen.Bookmarks.route) {
                            popUpTo(navController.graph.findStartDestination().id)
                            launchSingleTop = true
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) {
                HomeScreen(navController)
            }
            composable(Screen.Search.route) {
                SearchScreen(navController)
            }
            composable(Screen.Bookmarks.route) {
                BookmarksScreen(navController)
            }
            composable(Screen.Detail.route,
                deepLinks = listOf(
                    navDeepLink {
                        uriPattern = "https://www.moviedbapp.com/movie/{movieId}"
                    }
                )) {
                DetailScreen(navController)
            }
        }
    }
}