package com.example.bookreadingtracker.data

import android.app.Application
import androidx.lifecycle.LiveData

class RecommendationsRepository(application: Application) {
    private val recommendationsDao = AppDatabase.getDatabase(application).recommendationsDao()

    val allRecommendations: LiveData<List<RecommendedBook>> = recommendationsDao.getAll()

    suspend fun insertRecommendation(book: RecommendedBook) {
        recommendationsDao.insert(book)
    }

    suspend fun deleteRecommendation(book: RecommendedBook) {
        recommendationsDao.delete(book)
    }
}