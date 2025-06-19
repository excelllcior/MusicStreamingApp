package com.example.musicstreamingapp.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.musicstreamingapp.ui.enums.RepeatMode

@Composable
fun PlayerControls(
    isPlaying: Boolean,
    onPlayPause: () -> Unit,
    onSkipForward: () -> Unit,
    onSkipBack: () -> Unit,
    isShuffled: Boolean,
    onShuffleToggle: () -> Unit,
    repeatMode: RepeatMode,
    onRepeatToggle: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onShuffleToggle) {
            Icon(
                Icons.Default.Refresh,
                contentDescription = "Shuffle",
                tint = if (isShuffled) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.onSurface
            )
        }

        IconButton(onClick = onSkipBack) {
            Icon(Icons.Default.KeyboardArrowLeft, contentDescription = "Previous track")
        }

        IconButton(
            onClick = onPlayPause,
            modifier = Modifier.size(64.dp)
        ) {
            Icon(
                if (isPlaying) Icons.Default.Done else Icons.Default.Close,
                contentDescription = if (isPlaying) "Pause" else "Play",
                modifier = Modifier.size(48.dp)
            )
        }

        IconButton(onClick = onSkipForward) {
            Icon(Icons.Default.KeyboardArrowRight, contentDescription = "Next track")
        }

        IconButton(onClick = onRepeatToggle) {
            Icon(
                when (repeatMode) {
                    RepeatMode.None -> Icons.Default.Refresh
                    RepeatMode.All -> Icons.Default.Search
                    RepeatMode.One -> Icons.Default.CheckCircle
                },
                contentDescription = "Repeat",
                tint = if (repeatMode != RepeatMode.None) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.onSurface
            )
        }
    }
}