package com.example.musicstreamingapp.domain.repository

import com.example.musicstreamingapp.domain.model.NetworkResult
import com.example.musicstreamingapp.domain.model.Track

interface TrackRepository {
    suspend fun getTracks(): NetworkResult<List<Track>>
    suspend fun searchTracks(query: String): NetworkResult<List<Track>>
}