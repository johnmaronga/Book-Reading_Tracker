package com.example.bookreadingtracker.ui.theme

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bookreadingtracker.R
import com.example.bookreadingtracker.data.RecommendedBook
import com.example.bookreadingtracker.viewmodels.RecommendationsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecommendationsScreen(
    viewModel: RecommendationsViewModel = viewModel()
) {
    // Collect all states from ViewModel
    val recommendedBooks by viewModel.recommendedBooks.collectAsStateWithLifecycle()
    val isPlaying by viewModel.isPlaying.collectAsStateWithLifecycle()
    val showAddDialog by viewModel.showAddDialog.collectAsStateWithLifecycle()
    val showSourceDialog by viewModel.showSourceDialog.collectAsStateWithLifecycle()
    val showDeleteDialog by viewModel.showDeleteDialog.collectAsStateWithLifecycle()
    val context = LocalContext.current

    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { viewModel.setShowAddDialog(true) },
                icon = { Icon(Icons.Default.Add, "Add Recommendation") },
                text = { Text("Add Recommendation") },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        },
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Book Recommendations") },
                actions = {
                    // Text-based sound toggle instead of icon
                    TextButton(
                        onClick = { viewModel.toggleSound() },
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = MaterialTheme.colorScheme.onSurface
                        )
                    ) {
                        Text(
                            text = if (isPlaying) "🔊" else "🔇",
                            modifier = Modifier
                                .clickable { viewModel.toggleSound() }
                                .padding(horizontal = 16.dp),
                            style = MaterialTheme.typography.titleLarge
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            Button(
                onClick = { viewModel.setShowSourceDialog(true) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                )
            ) {
                Text("Find Book Recommendations")
            }

            if (recommendedBooks.isEmpty()) {
                EmptyRecommendationsMessage()
            } else {
                RecommendedBooksList(
                    books = recommendedBooks,
                    onDelete = { viewModel.setShowDeleteDialog(it) }
                )
            }
        }
    }

    // Add Recommendation Dialog
    if (showAddDialog) {
        AddRecommendationDialog(
            onDismiss = { viewModel.setShowAddDialog(false) },
            onConfirm = { title, rating, review ->
                viewModel.addRecommendation(title, rating, review)
                viewModel.setShowAddDialog(false)
            }
        )
    }

    // Source Selection Dialog
    if (showSourceDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.setShowSourceDialog(false) },
            title = { Text("Select Source") },
            text = { Text("Choose where to find book recommendations") },
            confirmButton = {
                Button(
                    onClick = {
                        context.openUrl("https://www.goodreads.com/")
                        viewModel.setShowSourceDialog(false)
                    }
                ) {
                    Text("Goodreads")
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.setShowSourceDialog(false) }) {
                    Text("Cancel")
                }
            }
        )
    }

    // Delete Confirmation Dialog
    showDeleteDialog?.let { bookId ->
        AlertDialog(
            onDismissRequest = { viewModel.setShowDeleteDialog(null) },
            title = { Text("Delete Recommendation") },
            text = { Text("Are you sure you want to delete this recommendation?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.deleteRecommendation(bookId)
                    },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.setShowDeleteDialog(null) }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
private fun EmptyRecommendationsMessage() {
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
private fun RecommendedBooksList(
    books: List<RecommendedBook>,
    onDelete: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(books, key = { it.id }) { book ->
            RecommendedBookItem(
                book = book,
                onDelete = { onDelete(book.id) }
            )
        }
    }
}

@Composable
private fun RecommendedBookItem(
    book: RecommendedBook,
    onDelete: () -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = book.title,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.weight(1f)
                )
                IconButton(onClick = onDelete) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                RatingBar(rating = book.rating)
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "%.1f/5".format(book.rating))
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
private fun AddRecommendationDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, Float, String) -> Unit
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

                Text("Rating: %.1f/5".format(rating))
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
                        onConfirm(title, rating, review)
                    }
                },
                enabled = title.isNotBlank()
            ) {
                Text("Add")
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
private fun RatingBar(rating: Float) {
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

private fun Context.openUrl(url: String) {
    try {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        }
    } catch (e: Exception) {
        Toast.makeText(this, "Error opening link", Toast.LENGTH_SHORT).show()
    }
}