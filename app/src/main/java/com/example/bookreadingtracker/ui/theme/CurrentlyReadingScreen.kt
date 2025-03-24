package com.example.bookreadingtracker.ui.theme

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Text

@Composable
fun CurrentlyReadingScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Currently Reading",
            style = MaterialTheme.typography.headlineMedium
        )
        // Placeholder for book list
        Text("Books you're currently reading will appear here")
    }
}