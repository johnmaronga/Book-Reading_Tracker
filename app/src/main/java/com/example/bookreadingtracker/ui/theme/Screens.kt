package com.example.bookreadingtracker.ui.theme

sealed class Screens(val route: String) {
    data object Home : Screens("home")
    data object CurrentlyReading : Screens("currently_reading")
    data object WantToRead : Screens("want_to_read")
    data object FinishedBooks : Screens("finished_books")
    data object Reviews : Screens("reviews")
    data object Recommendations : Screens("recommendations")
}