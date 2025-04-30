package com.example.bookreadingtracker.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "recommended_books")
data class RecommendedBook(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val title: String,
    val rating: Float = 0f,
    val review: String = "",
    val addedDate: String = ""
)