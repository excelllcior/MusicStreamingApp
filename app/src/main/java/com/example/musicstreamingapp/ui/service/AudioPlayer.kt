package com.example.musicstreamingapp.ui.service

import android.content.Context
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.example.musicstreamingapp.domain.model.Track
import com.example.musicstreamingapp.ui.enums.PlaybackMode
import com.example.musicstreamingapp.ui.enums.RepeatMode
import com.example.musicstreamingapp.ui.viewmodels.AudioPlayerState
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AudioPlayer @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private var exoPlayer: ExoPlayer? = null
    private var currentTrack: Track? = null
    private var currentQueue: List<Track> = emptyList()

    init {
        initializePlayer()
    }

    fun getCurrentState(): AudioPlayerState {
        return AudioPlayerState(
            playbackMode = if (isPlaying()) PlaybackMode.Playing else PlaybackMode.Paused,
            currentTrack = currentTrack,
            currentTrackProgress = getCurrentPosition().toInt(),
            isShuffled = exoPlayer?.shuffleModeEnabled == true,
            repeatMode = when (exoPlayer?.repeatMode) {
                Player.REPEAT_MODE_OFF -> RepeatMode.None
                Player.REPEAT_MODE_ALL -> RepeatMode.All
                Player.REPEAT_MODE_ONE -> RepeatMode.One
                else -> RepeatMode.None
            },
            queue = currentQueue
        )
    }

    private fun initializePlayer() {
        exoPlayer = ExoPlayer.Builder(context)
            .setHandleAudioBecomingNoisy(true)
            .build()
            .apply {
            addListener(object : Player.Listener {
                override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                    super.onMediaItemTransition(mediaItem, reason)
                    currentTrack = currentQueue.getOrNull(currentMediaItemIndex)
                }

                override fun onIsPlayingChanged(isPlaying: Boolean) {
                    super.onIsPlayingChanged(isPlaying)
                    // Update playback state if needed
                }
            })
        }
    }

    fun play(track: Track?) {
        track?.let {
            currentTrack = it
            exoPlayer?.let { player ->
                player.clearMediaItems()
                player.setMediaItem(createMediaItem(it))
                player.prepare()
                player.play()
            }
        }
    }

    fun addToQueue(tracks: List<Track>) {
        currentQueue = currentQueue + tracks
        exoPlayer?.let { player ->
            tracks.forEach { track ->
                player.addMediaItem(createMediaItem(track))
            }
        }
    }

    fun clearQueue() {
        currentQueue = emptyList()
        exoPlayer?.clearMediaItems()
    }

    fun pause() {
        exoPlayer?.pause()
    }
    fun resume() {
        exoPlayer?.play()
    }

    fun skipToNext() {
        exoPlayer?.seekToNextMediaItem()
    }

    fun skipToPrevious() {
        exoPlayer?.seekToPreviousMediaItem()
    }

    fun seekTo(position: Long) {
        exoPlayer?.seekTo(position)
    }

    fun getCurrentPosition(): Long = exoPlayer?.currentPosition ?: 0L

    fun getDuration(): Long = exoPlayer?.duration ?: 0L

    fun isPlaying(): Boolean = exoPlayer?.isPlaying == true

    fun getCurrentTrack(): Track? = currentTrack

    fun getQueue(): List<Track> = currentQueue

    fun setRepeatMode(repeatMode: RepeatMode) {
        exoPlayer?.repeatMode = when (repeatMode) {
            RepeatMode.None -> Player.REPEAT_MODE_OFF
            RepeatMode.All -> Player.REPEAT_MODE_ALL
            RepeatMode.One -> Player.REPEAT_MODE_ONE
        }
    }

    fun setShuffleModeEnabled(enabled: Boolean) {
        exoPlayer?.shuffleModeEnabled = enabled
    }

    private fun createMediaItem(track: Track): MediaItem {
        return MediaItem.Builder()
            .setUri(track.audioUrl)
            .setMediaId(track.id.toString())
            .setTag(track)
            .build()
    }

    fun release() {
        exoPlayer?.release()
        exoPlayer = null
    }
}