package com.example.bookreadingtracker.ui.theme

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.*

// Add this data class if not already present
data class RecommendedBook(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val rating: Float = 0f,
    val review: String = "",
    val addedDate: String = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(Date())
)

@Composable
fun RecommendationsScreen() {
    var recommendedBooks by remember { mutableStateOf<List<RecommendedBook>>(emptyList()) }
    var showAddDialog by remember { mutableStateOf(false) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Recommendation",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            Text(
                text = "Recommendations",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (recommendedBooks.isEmpty()) {
                EmptyRecommendationsMessage()
            } else {
                RecommendedBooksList(books = recommendedBooks)
            }
        }
    }

    if (showAddDialog) {
        AddRecommendationDialog(
            onDismiss = { showAddDialog = false },
            onConfirm = { newBook ->
                recommendedBooks = recommendedBooks + newBook
                showAddDialog = false
            }
        )
    }
}

@Composable
fun EmptyRecommendationsMessage() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "No recommendations yet",
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = "Add books you'd recommend to others",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}

@Composable
fun RecommendedBooksList(books: List<RecommendedBook>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(books) { book ->
            RecommendedBookItem(book = book)
        }
    }
}

@Composable
fun RecommendedBookItem(book: RecommendedBook) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = book.title,
                style = MaterialTheme.typography.titleLarge
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                RatingBar(rating = book.rating)
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "${"%.1f".format(book.rating)}/5")
            }

            if (book.review.isNotBlank()) {
                Text(
                    text = book.review,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            Text(
                text = "Added ${book.addedDate}",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

@Composable
fun AddRecommendationDialog(
    onDismiss: () -> Unit,
    onConfirm: (RecommendedBook) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var rating by remember { mutableFloatStateOf(0f) }
    var review by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Book Recommendation") },
        text = {
            Column(
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Book Title*") },
                    modifier = Modifier.fillMaxWidth()
                )

                Text("Rating: ${"%.1f".format(rating)}/5")
                Slider(
                    value = rating,
                    onValueChange = { rating = it },
                    valueRange = 0f..5f,
                    steps = 4,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = review,
                    onValueChange = { review = it },
                    label = { Text("Why do you recommend this book?") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    maxLines = 5
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (title.isNotBlank()) {
                        onConfirm(RecommendedBook(
                            title = title,
                            rating = rating,
                            review = review
                        ))
                    }
                },
                enabled = title.isNotBlank()
            ) {
                Text("Add Recommendation")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun RatingBar(rating: Float) {
    Row {
        repeat(5) { index ->
            Icon(
                imageVector = if (index < rating) Icons.Filled.Star else Icons.Outlined.Star,
                contentDescription = null,
                tint = if (index < rating) Color(0xFFFFD700) else Color.Gray,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}