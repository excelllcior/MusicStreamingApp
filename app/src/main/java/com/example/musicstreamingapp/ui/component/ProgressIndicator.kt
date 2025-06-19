package com.example.musicstreamingapp.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun ProgressIndicator(
    progress: Int,
    duration: Int,
    onProgressChange: (Int) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Slider(
            value = progress.toFloat(),
            onValueChange = { newValue -> onProgressChange(newValue.toInt()) },
            valueRange = 0f..duration.toFloat(),
            modifier = Modifier.fillMaxWidth()
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = formatDuration(progress))
            Text(text = formatDuration(duration))
        }
    }
}

private fun formatDuration(milliseconds: Int): String {
    val seconds = milliseconds / 1000
    val minutes = seconds / 60
    val remainingSeconds = seconds % 60
    return String.format("%02d:%02d", minutes, remainingSeconds)
}