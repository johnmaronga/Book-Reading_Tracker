package com.example.bookreadingtracker.ui.theme

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.RateReview
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier



import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

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
                        Icon(Icons.Default.Menu, contentDescription = "Menu")
                    }
                }
            )
        },
        //drawerContent = {
          //  DrawerContent(navController)
        //}
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = Screens.Home.route,
            modifier = Modifier.padding(padding)
        ) {
            composable(Screens.Home.route) { HomeScreen { screen -> navController.navigate(screen.route) } }
            composable(Screens.CurrentlyReading.route) { CurrentlyReadingScreen() }
            composable(Screens.WantToRead.route) { WantToReadScreen() }
            composable(Screens.FinishedBooks.route) { FinishedBooksScreen() }
            composable(Screens.Reviews.route) { ReviewsScreen() }
            composable(Screens.Recommendations.route) { RecommendationsScreen() }
        }
    }
}

@Composable
fun DrawerContent(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Book Tracker",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(vertical = 16.dp)
        )

        Divider()

        // Navigation items
        NavigationItem(
            icon = Icons.Default.Home,
            label = "Home",
            onClick = { navController.navigate(Screens.Home.route) }
        )

        NavigationItem(
            icon = Icons.Default.Book,
            label = "Currently Reading",
            onClick = { navController.navigate(Screens.CurrentlyReading.route) }
        )

        NavigationItem(
            icon = Icons.Default.List,
            label = "Want to Read",
            onClick = { navController.navigate(Screens.WantToRead.route) }
        )

        NavigationItem(
            icon = Icons.Default.DoneAll,
            label = "Finished Books",
            onClick = { navController.navigate(Screens.FinishedBooks.route) }
        )

        NavigationItem(
            icon = Icons.Default.RateReview,
            label = "Reviews",
            onClick = { navController.navigate(Screens.Reviews.route) }
        )

        NavigationItem(
            icon = Icons.Default.Star,
            label = "Recommendations",
            onClick = { navController.navigate(Screens.Recommendations.route) }
        )
    }
}

@Composable
fun NavigationItem(icon: ImageVector, label: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = label, style = MaterialTheme.typography.bodyLarge)
    }
}