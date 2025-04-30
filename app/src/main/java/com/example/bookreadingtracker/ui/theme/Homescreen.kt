package com.example.bookreadingtracker.ui.theme


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@Composable
fun HomeScreen(onNavigate: (Screens) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Image()


        Text(
            text = "Book Tracker",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Quick stats or welcome message
        Text(
            text = "Track your reading journey",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        // Navigation buttons
        Button(
            onClick = { onNavigate(Screens.CurrentlyReading) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Currently Reading")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { onNavigate(Screens.WantToRead) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Want to Read")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { onNavigate(Screens.FinishedBooks) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Finished Books")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { onNavigate(Screens.Recommendations) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Recommendations")
        }
    }
}

fun Image() {

}

@Preview(
    name = "Home Screen Preview",  // Display name in Android Studio
    showBackground = true,        // Shows white background
    showSystemUi = false,         // Doesn't show status bar
    device = "id:pixel_5"         // Specific device to emulate
)
@Composable
fun HomeScreenPreview() {
    MaterialTheme {
        Surface {  // Wraps in Surface for proper theming
            HomeScreen(onNavigate = {})  // Empty lambda for preview
        }
    }
}