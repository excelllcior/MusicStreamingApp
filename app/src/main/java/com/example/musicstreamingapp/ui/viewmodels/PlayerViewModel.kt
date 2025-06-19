package com.example.musicstreamingapp.ui.viewmodels

import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicstreamingapp.domain.model.Track
import com.example.musicstreamingapp.domain.usecase.GetTracksUseCase
import com.example.musicstreamingapp.ui.enums.PlaybackMode
import com.example.musicstreamingapp.ui.enums.RepeatMode
import com.example.musicstreamingapp.ui.service.AudioPlayer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class AudioPlayerEvent {
    data class Play(val track: Track) : AudioPlayerEvent()
    object Pause : AudioPlayerEvent()
    object Resume : AudioPlayerEvent()
    object SkipBack : AudioPlayerEvent()
    object SkipForward : AudioPlayerEvent()
    data class UpdateProgress(val position: Int) : AudioPlayerEvent()
    object ToggleRepeatMode : AudioPlayerEvent()
    object ToggleShuffling : AudioPlayerEvent()
    data class AddToQueue(val track: Track) : AudioPlayerEvent()
}

data class AudioPlayerState(
    val playbackMode: PlaybackMode = PlaybackMode.None,
    val currentTrack: Track? = null,
    val currentTrackProgress: Int = 0,
    val isShuffled: Boolean = false,
    val repeatMode: RepeatMode = RepeatMode.None,
    val queue: List<Track> = emptyList()
)

@HiltViewModel
class PlayerViewModel @Inject constructor(
    private val audioPlayer: AudioPlayer,
    private val getTracksUseCase: GetTracksUseCase
) : ViewModel() {
    private val _playerState = MutableStateFlow(AudioPlayerState())
    val playerState: StateFlow<AudioPlayerState> = _playerState

    private val _screenState = MutableStateFlow<ScreenState>(ScreenState.TrackList)
    val screenState: StateFlow<ScreenState> = _screenState

    sealed class ScreenState {
        object TrackList : ScreenState()
        data class JamSession(val sessionId: Int) : ScreenState()
    }

    private var progressUpdateJob: Job? = null

    init {
        startProgressUpdates()
        observePlayerChanges()
    }



    private fun startProgressUpdates() {
        progressUpdateJob?.cancel()
        progressUpdateJob = viewModelScope.launch {
            while (true) {
                delay(500)
                if (audioPlayer.isPlaying()) {
                    _playerState.update { state ->
                        state.copy(
                            currentTrackProgress = audioPlayer.getCurrentPosition().toInt(),
                            playbackMode = PlaybackMode.Playing
                        )
                    }
                }
            }
        }
    }

    private fun observePlayerChanges() {
        viewModelScope.launch {
            snapshotFlow { audioPlayer.getCurrentTrack() }
                .collect { track ->
                    _playerState.update { state ->
                        state.copy(currentTrack = track)
                    }
                }
        }

        viewModelScope.launch {
            snapshotFlow { audioPlayer.getQueue() }
                .collect { queue ->
                    _playerState.update { state ->
                        state.copy(queue = queue)
                    }
                }
        }
    }

    fun onEvent(event: AudioPlayerEvent) {
        when (event) {
            is AudioPlayerEvent.Play -> handlePlay(event.track)
            AudioPlayerEvent.Pause -> handlePause()
            AudioPlayerEvent.Resume -> handleResume()
            AudioPlayerEvent.SkipBack -> handleSkipBack()
            AudioPlayerEvent.SkipForward -> handleSkipForward()
            is AudioPlayerEvent.UpdateProgress -> handleUpdateProgress(event.position)
            AudioPlayerEvent.ToggleRepeatMode -> toggleRepeatMode()
            AudioPlayerEvent.ToggleShuffling -> toggleShuffling()
            is AudioPlayerEvent.AddToQueue -> addToQueue(event.track)
        }
    }

    private fun handlePlay(track: Track) {
        audioPlayer.play(track)
        _playerState.update { state ->
            state.copy(
                playbackMode = PlaybackMode.Playing,
                currentTrack = track,
                queue = emptyList()
            )
        }
    }

    private fun handlePause() {
        audioPlayer.pause()
        _playerState.update { state ->
            state.copy(playbackMode = PlaybackMode.Paused)
        }
    }

    private fun handleResume() {
        audioPlayer.resume()
        _playerState.update { state ->
            state.copy(playbackMode = PlaybackMode.Playing)
        }
    }

    private fun handleSkipBack() {
        audioPlayer.skipToPrevious()
    }

    private fun handleSkipForward() {
        audioPlayer.skipToNext()
    }

    private fun handleUpdateProgress(position: Int) {
        audioPlayer.seekTo(position.toLong())
        _playerState.update { state ->
            state.copy(currentTrackProgress = position)
        }
    }

    private fun toggleRepeatMode() {
        val newMode = when (_playerState.value.repeatMode) {
            RepeatMode.None -> RepeatMode.All
            RepeatMode.All -> RepeatMode.One
            RepeatMode.One -> RepeatMode.None
        }
        audioPlayer.setRepeatMode(newMode)
        _playerState.update { state ->
            state.copy(repeatMode = newMode)
        }
    }

    private fun toggleShuffling() {
        val newShuffleState = !_playerState.value.isShuffled
        audioPlayer.setShuffleModeEnabled(newShuffleState)
        _playerState.update { state ->
            state.copy(isShuffled = newShuffleState)
        }
    }

    private fun addToQueue(track: Track) {
        audioPlayer.addToQueue(listOf(track))
    }

    fun playTrackWithQueue(track: Track, remainingTracks: List<Track>) {
        audioPlayer.play(track)
        audioPlayer.addToQueue(remainingTracks)
        _playerState.update { state ->
            state.copy(
                playbackMode = PlaybackMode.Playing,
                currentTrack = track,
                queue = remainingTracks
            )
        }
    }

    override fun onCleared() {
        super.onCleared()
        progressUpdateJob?.cancel()
        audioPlayer.release()
    }
}