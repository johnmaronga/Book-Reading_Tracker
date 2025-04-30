package com.example.bookreadingtracker.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookreadingtracker.data.RecommendedBook
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class RecommendationsViewModel(application: Application) : AndroidViewModel(application) {
    // Book recommendations state
    private val _recommendedBooks = MutableStateFlow<List<RecommendedBook>>(emptyList())
    val recommendedBooks: StateFlow<List<RecommendedBook>> = _recommendedBooks.asStateFlow()

    // Sound playback state
    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()

    // Dialog visibility states
    private val _showAddDialog = MutableStateFlow(false)
    val showAddDialog: StateFlow<Boolean> = _showAddDialog.asStateFlow()

    private val _showSourceDialog = MutableStateFlow(false)
    val showSourceDialog: StateFlow<Boolean> = _showSourceDialog.asStateFlow()

    private val _showDeleteDialog = MutableStateFlow<String?>(null)
    val showDeleteDialog: StateFlow<String?> = _showDeleteDialog.asStateFlow()

    // Add a new book recommendation
    fun addRecommendation(title: String, rating: Float, review: String) {
        val newBook = RecommendedBook(
            title = title,
            rating = rating,
            review = review,
            addedDate = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(Date())
        )
        _recommendedBooks.update { current -> current + newBook }
    }

    // Delete a book recommendation
    fun deleteRecommendation(bookId: String) {
        _recommendedBooks.update { current ->
            current.filter { it.id != bookId }
        }
        _showDeleteDialog.value = null
    }

    // Toggle sound playback state
    fun toggleSound() {
        _isPlaying.update { !it }
    }

    // Control dialog visibility
    fun setShowAddDialog(show: Boolean) {
        _showAddDialog.value = show
    }

    fun setShowSourceDialog(show: Boolean) {
        _showSourceDialog.value = show
    }

    fun setShowDeleteDialog(bookId: String?) {
        _showDeleteDialog.value = bookId
    }
}