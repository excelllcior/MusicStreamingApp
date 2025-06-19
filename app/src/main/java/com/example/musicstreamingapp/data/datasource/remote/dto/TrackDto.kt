package com.example.musicstreamingapp.data.datasource.remote.dto

import com.example.musicstreamingapp.domain.model.Track
import com.google.gson.annotations.SerializedName

data class TrackDto(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("image_url") val imageUrl: String? = null,
    @SerializedName("audio_url") val audioUrl: String,
    @SerializedName("duration") val duration: Int,
    @SerializedName("lyrics") val lyrics: String? = null,
    @SerializedName("genre") val genre: GenreDto
) {
    fun toTrack(): Track = Track(
        id = id,
        name = name,
        imageUrl = imageUrl,
        audioUrl = audioUrl,
        duration = duration,
        lyrics = lyrics,
        genre = genre.toGenre()
    )
}
