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

// Data class for want-to-read books
data class WantToReadBook(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val author: String,
    val genre: String,
    val addedDate: String = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(Date())
)

// Common book genres
val bookGenres = listOf(
    "Fiction",
    "Non-Fiction",
    "Fantasy",
    "Science Fiction",
    "Mystery",
    "Romance",
    "Thriller",
    "Biography",
    "History",
    "Self-Help",
    "Other"
)

@Composable
fun WantToReadScreen() {
    var wantToReadBooks by remember { mutableStateOf<List<WantToReadBook>>(emptyList()) }
    var showAddDialog by remember { mutableStateOf(false) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Want-to-Read Book",
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
                text = "Want to Read",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (wantToReadBooks.isEmpty()) {
                EmptyWantToReadMessage()
            } else {
                WantToReadBooksList(books = wantToReadBooks)
            }
        }
    }

    if (showAddDialog) {
        AddWantToReadDialog(
            onDismiss = { showAddDialog = false },
            onConfirm = { newBook ->
                wantToReadBooks = wantToReadBooks + newBook
                showAddDialog = false
            }
        )
    }
}

@Composable
fun EmptyWantToReadMessage() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Your wishlist is empty",
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = "Add books you'd like to read",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}

@Composable
fun WantToReadBooksList(books: List<WantToReadBook>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(books) { book ->
            WantToReadBookItem(book = book)
        }
    }
}

@Composable
fun WantToReadBookItem(book: WantToReadBook) {
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

            // Genre chip
            FilterChip(
                selected = true,
                onClick = { },
                label = { Text(book.genre) },
                modifier = Modifier.padding(top = 8.dp)
            )

            Text(
                text = "Added ${book.addedDate}",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddWantToReadDialog(
    onDismiss: () -> Unit,
    onConfirm: (WantToReadBook) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var author by remember { mutableStateOf("") }
    var selectedGenre by remember { mutableStateOf(bookGenres[0]) }
    var genreExpanded by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Want-to-Read Book") },
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

                // Genre dropdown
                ExposedDropdownMenuBox(
                    expanded = genreExpanded,
                    onExpandedChange = { genreExpanded = !genreExpanded }
                ) {
                    OutlinedTextField(
                        value = selectedGenre,
                        onValueChange = {},
                        label = { Text("Genre") },
                        readOnly = true,
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = genreExpanded)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                    )

                    ExposedDropdownMenu(
                        expanded = genreExpanded,
                        onDismissRequest = { genreExpanded = false }
                    ) {
                        bookGenres.forEach { genre ->
                            DropdownMenuItem(
                                text = { Text(genre) },
                                onClick = {
                                    selectedGenre = genre
                                    genreExpanded = false
                                }
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (title.isNotBlank()) {
                        onConfirm(WantToReadBook(
                            title = title,
                            author = author,
                            genre = selectedGenre
                        ))
                    }
                },
                enabled = title.isNotBlank()
            ) {
                Text("Add to List")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}