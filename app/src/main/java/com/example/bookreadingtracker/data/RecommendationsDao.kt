package com.example.bookreadingtracker.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface RecommendationsDao {
    @Query("SELECT * FROM recommended_books ORDER BY addedDate DESC")
    fun getAll(): LiveData<List<RecommendedBook>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(book: RecommendedBook)

    @Delete
    suspend fun delete(book: RecommendedBook)
}