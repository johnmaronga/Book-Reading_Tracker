package com.example.bookreadingtracker.ui.theme

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.*

// Data class for currently reading books
data class CurrentBook(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val author: String,
    val currentPage: Int,
    val totalPages: Int? = null,
    val addedDate: String = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(Date())
)

@Composable
fun CurrentlyReadingScreen() {
    var currentBooks by remember { mutableStateOf<List<CurrentBook>>(emptyList()) }
    var showAddDialog by remember { mutableStateOf(false) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Current Book",
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
                text = "Currently Reading",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (currentBooks.isEmpty()) {
                EmptyCurrentBooksMessage()
            } else {
                CurrentBooksList(books = currentBooks)
            }
        }
    }

    if (showAddDialog) {
        AddCurrentBookDialog(
            onDismiss = { showAddDialog = false },
            onConfirm = { newBook ->
                currentBooks = currentBooks + newBook
                showAddDialog = false
            }
        )
    }
}

@Composable
fun EmptyCurrentBooksMessage() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "No books in progress",
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = "Add books you're currently reading",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}

@Composable
fun CurrentBooksList(books: List<CurrentBook>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(books) { book ->
            CurrentBookItem(book = book)
        }
    }
}

@Composable
fun CurrentBookItem(book: CurrentBook) {
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

            // Page progress display
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                Text(
                    text = "Page ${book.currentPage}",
                    style = MaterialTheme.typography.bodyMedium
                )
                book.totalPages?.let { total ->
                    Text(
                        text = " of $total",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "${(book.currentPage.toFloat() / total * 100).toInt()}%",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
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
fun AddCurrentBookDialog(
    onDismiss: () -> Unit,
    onConfirm: (CurrentBook) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var author by remember { mutableStateOf("") }
    var currentPage by remember { mutableStateOf("") }
    var totalPages by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Current Book") },
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

                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    OutlinedTextField(
                        value = currentPage,
                        onValueChange = { currentPage = it.filter { c -> c.isDigit() } },
                        label = { Text("Current Page*") },
                        modifier = Modifier.weight(1f)
                    )

                    OutlinedTextField(
                        value = totalPages,
                        onValueChange = { totalPages = it.filter { c -> c.isDigit() } },
                        label = { Text("Total Pages") },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (title.isNotBlank() && currentPage.isNotBlank()) {
                        onConfirm(CurrentBook(
                            title = title,
                            author = author,
                            currentPage = currentPage.toIntOrNull() ?: 1,
                            totalPages = totalPages.toIntOrNull()
                        ))
                    }
                },
                enabled = title.isNotBlank() && currentPage.isNotBlank()
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