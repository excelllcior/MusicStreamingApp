package com.example.musicstreamingapp.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.example.musicstreamingapp.domain.model.Track
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

enum class RepeatMode {
    None, All, Single
}

data class AppUiState(
    val isPlaying: Boolean = false,
    val currentTrack: Track? = null,
    val currentPosition: Int = 0, // Current Track Position
    val isShuffled: Boolean = false,
    val repeatMode: RepeatMode = RepeatMode.None,
    val queue: List<Track> = emptyList()
)

sealed class AppUiEvent {
    object Play: AppUiEvent()
    object Pause: AppUiEvent()
    data class SeekTo(val position: Int): AppUiEvent()
    object SkipBack: AppUiEvent()
    object SkipForward: AppUiEvent()
    object ToggleShuffle: AppUiEvent()
    data class ChangeRepeatMode(val mode: RepeatMode) : AppUiEvent()
    data class AddToQueue(val track: Track) : AppUiEvent()
    object ClearQueue : AppUiEvent()
}

class AppViewModel(context: Context) : ViewModel(

) {
    private val _uiState = MutableStateFlow(AppUiState())
    val uiState: StateFlow<AppUiState> = _uiState.asStateFlow()

    private val exoPlayer: ExoPlayer = ExoPlayer.Builder(context).build().apply {
        addListener(object: Player.Listener {
            override fun onPlaybackStateChanged(playbackState: Int) {
                when (playbackState) {
                    Player.STATE_ENDED -> handleEvent(AppUiEvent.SkipForward)
                }
            }
        })
    }

    private var progressUpdateJob: Job? = null

    fun handleEvent(event: AppUiEvent) {
        when (event) {
            is AppUiEvent.AddToQueue -> TODO()
            is AppUiEvent.ChangeRepeatMode -> TODO()
            AppUiEvent.Pause -> TODO()
            AppUiEvent.Play -> TODO()
            is AppUiEvent.SeekTo -> TODO()
            AppUiEvent.SkipBack -> TODO()
            AppUiEvent.SkipForward -> TODO()
            AppUiEvent.ToggleShuffle -> TODO()
            else -> TODO()
        }
    }

    private fun seekTo(position: Int) {
        _uiState.value = _uiState.value.copy(
            currentPosition = position
        )
    }

    private fun togglePlay() {
        if (exoPlayer.isPlaying) {
            exoPlayer.pause()
            _uiState.value = _uiState.value.copy(isPlaying = true)
        }
        else {
            exoPlayer.play()
            _uiState.value = _uiState.value.copy(isPlaying = false)
        }
    }
}