package com.example.musicstreamingapp.data.datasource.remote.dto

import com.example.musicstreamingapp.domain.model.Genre
import com.google.gson.annotations.SerializedName

data class GenreDto(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String
) {
    fun toGenre(): Genre = Genre(id, name)
}