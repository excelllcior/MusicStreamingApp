package com.example.musicstreamingapp.ui.service

import android.content.Context
import com.example.musicstreamingapp.domain.model.Track
import com.example.musicstreamingapp.ui.viewmodels.AudioPlayerState
import com.example.musicstreamingapp.util.PreferencesManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import javax.inject.Inject
import javax.inject.Singleton

@Serializable
data class JamSession(
    val sessionId: Int,
    val hostId: Int,
    val members: List<Int>,
    val playerState: AudioPlayerState
)

@Serializable
data class JamMessage(
    val type: JamMessageType,
    val senderId: Int,
    val payload: Any? = null
)

@Serializable
enum class JamMessageType {
    JOIN_SESSION,
    LEAVE_SESSION,
    UPDATE_PLAYER_STATE,
    ADD_TO_QUEUE,
    HOST_TRANSFER
}

@Singleton
class JamSessionService @Inject constructor(
    private val context: Context,
    private val audioPlayer: AudioPlayer,
    private val preferencesManager: PreferencesManager
) {
    private var webSocket: WebSocket? = null
    private var isHost: Boolean = false
    private var sessionId: Int? = null

    private val _jamState = MutableStateFlow<JamSession?>(null)
    val jamState: StateFlow<JamSession?> = _jamState

    private val userId: Int by lazy {
        preferencesManager.getUserId()
    }

    fun startJamSession(sessionId: Int) {
        this.sessionId = sessionId
        this.isHost = true
        connectWebSocket()
    }

    fun joinJamSession(sessionId: Int) {
        this.sessionId = sessionId
        this.isHost = false
        connectWebSocket()
    }

    fun leaveJamSession() {
        sendMessage(JamMessage(
            type = JamMessageType.LEAVE_SESSION,
            senderId = userId
        ))
        webSocket?.close(1000, "User left")
        webSocket = null
        _jamState.value = null
    }

    fun updatePlayerState(state: AudioPlayerState) {
        if (isHost) {
            sendMessage(JamMessage(
                type = JamMessageType.UPDATE_PLAYER_STATE,
                senderId = userId,
                payload = state
            ))
        }
    }

    fun addToQueue(track: Track) {
        sendMessage(JamMessage(
            type = JamMessageType.ADD_TO_QUEUE,
            senderId = userId,
            payload = track
        ))
    }

    private fun connectWebSocket() {
        val request = Request.Builder()
            .url("ws://10.0.2.2:8000/ws/jam-session/$sessionId") // 10.0.2.2 is localhost for emulator
            .build()

        val listener = object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                this@JamSessionService.webSocket = webSocket
                sendMessage(JamMessage(
                    type = if (isHost) JamMessageType.HOST_TRANSFER else JamMessageType.JOIN_SESSION,
                    senderId = userId,
                    payload = if (isHost) audioPlayer.getCurrentState() else null
                ))
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                handleMessage(text)
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                // Handle failure
            }
        }

        OkHttpClient().newWebSocket(request, listener)
    }

    private fun handleMessage(message: String) {
        try {
            val jamMessage = Json.decodeFromString<JamMessage>(message)

            when (jamMessage.type) {
                JamMessageType.UPDATE_PLAYER_STATE -> {
                    val state = jamMessage.payload as AudioPlayerState
                    _jamState.value = _jamState.value?.copy(playerState = state)
                    if (!isHost) {
                        // Sync player with host state
                        audioPlayer.play(state.currentTrack)
                        audioPlayer.clearQueue()
                        audioPlayer.addToQueue(state.queue)
                    }
                }
                JamMessageType.ADD_TO_QUEUE -> {
                    val track = jamMessage.payload as Track
                    if (isHost) {
                        audioPlayer.addToQueue(listOf(track))
                    }
                }
                JamMessageType.JOIN_SESSION -> {
                    _jamState.value = _jamState.value?.copy(
                        members = _jamState.value!!.members + jamMessage.senderId
                    )
                }
                JamMessageType.LEAVE_SESSION -> {
                    _jamState.value = _jamState.value?.copy(
                        members = _jamState.value!!.members - jamMessage.senderId
                    )
                }
                JamMessageType.HOST_TRANSFER -> {
                    val initialState = jamMessage.payload as AudioPlayerState
                    _jamState.value = JamSession(
                        sessionId = sessionId!!,
                        hostId = jamMessage.senderId,
                        members = listOf(jamMessage.senderId, userId),
                        playerState = initialState
                    )
                    if (!isHost) {
                        audioPlayer.play(initialState.currentTrack)
                        audioPlayer.clearQueue()
                        audioPlayer.addToQueue(initialState.queue)
                    }
                }
                else -> {}
            }
        } catch (e: Exception) {
            // Handle error
        }
    }

    private fun sendMessage(message: JamMessage) {
        try {
            val json = Json.encodeToString(message)
            webSocket?.send(json)
        } catch (e: Exception) {
            // Handle error
        }
    }
}