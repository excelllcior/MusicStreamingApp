package com.example.musicstreamingapp.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.musicstreamingapp.R
import com.example.musicstreamingapp.domain.model.Track
import com.example.musicstreamingapp.ui.enums.PlaybackMode
import com.example.musicstreamingapp.ui.enums.RepeatMode
import com.example.musicstreamingapp.ui.viewmodels.AudioPlayerEvent
import com.example.musicstreamingapp.ui.viewmodels.AudioPlayerState

@Composable
fun PlayerBottomSheet(
    modifier: Modifier,
    playerState: AudioPlayerState,
    onEvent: (AudioPlayerEvent) -> Unit
) {
    val pagerState = rememberPagerState { playerState.queue.size }

    LaunchedEffect(playerState.currentTrack) {
        playerState.currentTrack?.let {
            pagerState.scrollToPage(0)
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.Black)
            .padding(16.dp)
    ) {
        Box {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
            ) { page ->
                val track = when (page) {
                    0 -> playerState.currentTrack
                    else -> playerState.queue.getOrNull(page - 1)
                }

                AsyncImage(
                    model = track?.imageUrl,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.surfaceContainerLow)
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            playerState.currentTrack?.let { track ->
                Text(
                    text = track.name,
                    style = MaterialTheme.typography.titleLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = track.genre.name,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        Spacer(modifier = Modifier.height(24.dp))

        var sliderPosition by remember { mutableFloatStateOf(0f) }
        var isSliderDragging by remember { mutableStateOf(false) }

        LaunchedEffect(playerState.currentTrackProgress, isSliderDragging) {
            if (!isSliderDragging) {
                sliderPosition = playerState.currentTrackProgress.toFloat()
            }
        }

        Column {
            Slider(
                value = sliderPosition,
                onValueChange = {
                    sliderPosition = it
                    isSliderDragging = true
                },
                onValueChangeFinished = {
                    isSliderDragging = false
                    onEvent(AudioPlayerEvent.UpdateProgress(sliderPosition.toInt()))
                },
                valueRange = 0f..(playerState.currentTrack?.duration?.toFloat() ?: 0f),
                modifier = Modifier.fillMaxWidth()
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = (playerState.currentTrackProgress / 1000).toFormattedDuration(),
                    style = MaterialTheme.typography.labelMedium
                )
                Text(
                    text = ((playerState.currentTrack?.duration ?: 0) / 1000).toFormattedDuration(),
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }
        Spacer(modifier = Modifier.height(24.dp))

        PlayerControls(
            playerState = playerState,
            onEvent = onEvent,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun PlayerControls(
    playerState: AudioPlayerState,
    onEvent: (AudioPlayerEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = { onEvent(AudioPlayerEvent.ToggleShuffling) },
            modifier = Modifier.size(48.dp)
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_shuffle),
                contentDescription = "Shuffle",
                modifier = Modifier.size(32.dp),
                tint = if (playerState.isShuffled) Color.White
                else Color.White.copy(alpha = 0.65f)
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        IconButton(
            onClick = { onEvent(AudioPlayerEvent.SkipBack) },
            modifier = Modifier.size(48.dp)
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_skip_back),
                contentDescription = "Previous",
                modifier = Modifier.size(40.dp),
                tint = Color.White
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        IconButton(
            onClick = {
                if (playerState.playbackMode == PlaybackMode.Playing) {
                    onEvent(AudioPlayerEvent.Pause)
                } else {
                    onEvent(AudioPlayerEvent.Resume)
                }
            },
            modifier = Modifier.size(80.dp)
        ) {
            Icon(
                imageVector = if (playerState.playbackMode == PlaybackMode.Playing) {
                    ImageVector.vectorResource(R.drawable.ic_pause_circle)
                } else {
                    ImageVector.vectorResource(R.drawable.ic_play_circle)
                },
                contentDescription = "Play/Pause",
                modifier = Modifier.size(72.dp),
                tint = Color.White
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        IconButton(
            onClick = { onEvent(AudioPlayerEvent.SkipForward) },
            modifier = Modifier.size(48.dp),
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_skip_forward),
                contentDescription = "Next",
                modifier = Modifier.size(40.dp),
                tint = Color.White
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        IconButton(
            onClick = { onEvent(AudioPlayerEvent.ToggleRepeatMode) },
            modifier = Modifier.size(48.dp)
        ) {
            Icon(
                imageVector = when (playerState.repeatMode) {
                    RepeatMode.None -> ImageVector.vectorResource(R.drawable.ic_repeat)
                    RepeatMode.All -> ImageVector.vectorResource(R.drawable.ic_repeat)
                    RepeatMode.One -> ImageVector.vectorResource(R.drawable.ic_repeat_one)
                },
                contentDescription = "Repeat",
                modifier = Modifier.size(32.dp),
                tint = if (playerState.repeatMode == RepeatMode.None) Color.White.copy(alpha = 0.65f)
                else Color.White
            )
        }
    }
}

@Composable
fun SheetHandle(currentTrack: Track) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { /* Handle click to expand */ },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = currentTrack.imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .size(40.dp)
                    .clip(MaterialTheme.shapes.small),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = currentTrack.name,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "Now Playing",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            IconButton(
                onClick = { /* Toggle play/pause */ },
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_play),
                    contentDescription = "Pause"
                )
            }
        }
    }
}

fun Int.toFormattedDuration(): String {
    val minutes = this / 60
    val seconds = this % 60
    return String.format("%d:%02d", minutes, seconds)
}