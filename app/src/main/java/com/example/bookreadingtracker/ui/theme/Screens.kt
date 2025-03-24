package com.example.bookreadingtracker.ui.theme

sealed class Screens(val route: String) {
    object Home : Screens("home")
    object CurrentlyReading : Screens("currently_reading")
    object WantToRead : Screens("want_to_read")
    object FinishedBooks : Screens("finished_books")
    object Reviews : Screens("reviews")
    object Recommendations : Screens("recommendations")
}