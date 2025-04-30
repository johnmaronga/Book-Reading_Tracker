package com.example.bookreadingtracker.ui.theme

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

//sealed class Screens(val route: String) {
//    object Home : Screens("home")
//    object CurrentlyReading : Screens("currently_reading")
//    object WantToRead : Screens("want_to_read")
//    object FinishedBooks : Screens("finished_books")
//    object Reviews : Screens("reviews")
//    object Recommendations : Screens("recommendations")
//}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookTrackerNavigation() {
    val navController = rememberNavController()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Book Tracker") },
                navigationIcon = {
                    IconButton(onClick = { /* Handle menu toggle */ }) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Menu"
                        )
                    }
                }
            )
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = Screens.Home.route,
            modifier = Modifier.padding(padding)
        ) {
            composable(Screens.Home.route) {
                HomeScreen { screen -> navController.navigate(screen.route) }
            }
            composable(Screens.CurrentlyReading.route) { CurrentlyReadingScreen() }
            composable(Screens.WantToRead.route) { WantToReadScreen() }
            composable(Screens.FinishedBooks.route) { FinishedBooksScreen() }
            composable(Screens.Reviews.route) { ReviewsScreen() }
            composable(Screens.Recommendations.route) { RecommendationsScreen() }
        }
    }
}