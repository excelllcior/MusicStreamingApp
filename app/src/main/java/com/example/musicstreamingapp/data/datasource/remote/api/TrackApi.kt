package com.example.musicstreamingapp.data.datasource.remote.api

import com.example.musicstreamingapp.data.datasource.remote.dto.TrackDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TrackApi {
    @GET("tracks/")
    suspend fun getTracks(): List<TrackDto>

    @GET("tracks/")
    suspend fun searchTracks(@Query("query") query: String): List<TrackDto>
}