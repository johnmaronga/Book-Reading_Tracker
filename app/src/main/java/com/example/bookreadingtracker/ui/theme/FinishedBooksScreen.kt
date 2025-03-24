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

// Data class for finished books
data class FinishedBook(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val author: String,
    val rating: Float = 0f,
    val finishedDate: String = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(Date())
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FinishedBooksScreen() {
    var finishedBooks by remember { mutableStateOf<List<FinishedBook>>(emptyList()) }
    var showAddDialog by remember { mutableStateOf(false) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Finished Book",
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
                text = "Finished Books",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (finishedBooks.isEmpty()) {
                EmptyFinishedBooksMessage()
            } else {
                FinishedBooksList(books = finishedBooks)
            }
        }
    }

    if (showAddDialog) {
        AddFinishedBookDialog(
            onDismiss = { showAddDialog = false },
            onConfirm = { newBook ->
                finishedBooks = finishedBooks + newBook
                showAddDialog = false
            }
        )
    }
}

@Composable
fun EmptyFinishedBooksMessage() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "No finished books yet",
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = "Add books you've completed reading",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}

@Composable
fun FinishedBooksList(books: List<FinishedBook>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(books) { book ->
            FinishedBookItem(book = book)
        }
    }
}

@Composable
fun FinishedBookItem(book: FinishedBook) {
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

            if (book.author.isNotBlank()) {
                Text(
                    text = "by ${book.author}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 8.dp)
            ) {

                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "${"%.1f".format(book.rating)}/5")
            }

            Text(
                text = "Finished ${book.finishedDate}",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddFinishedBookDialog(
    onDismiss: () -> Unit,
    onConfirm: (FinishedBook) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var author by remember { mutableStateOf("") }
    var rating by remember { mutableStateOf(0f) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Finished Book") },
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

                OutlinedTextField(
                    value = author,
                    onValueChange = { author = it },
                    label = { Text("Author") },
                    modifier = Modifier.fillMaxWidth()
                )

                Text("Your Rating: ${"%.1f".format(rating)}/5")
                Slider(
                    value = rating,
                    onValueChange = { rating = it },
                    valueRange = 0f..5f,
                    steps = 4,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (title.isNotBlank()) {
                        onConfirm(FinishedBook(
                            title = title,
                            author = author,
                            rating = rating
                        ))
                    }
                },
                enabled = title.isNotBlank()
            ) {
                Text("Add Book")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}


