package com.example.musicstreamingapp.domain.model

data class Track(
    val id: Int,
    val name: String,
    val imageUrl: String? = null,
    val audioUrl: String,
    val duration: Int,
    val lyrics: String? = null,
    val genre: Genre
)

