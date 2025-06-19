package com.example.musicstreamingapp.data.repository

import com.example.musicstreamingapp.data.datasource.remote.api.TrackApi
import com.example.musicstreamingapp.domain.model.NetworkResult
import com.example.musicstreamingapp.domain.model.Track
import com.example.musicstreamingapp.domain.repository.TrackRepository

class TrackRepositoryImpl(
    private val api: TrackApi
) : TrackRepository {
    override suspend fun getTracks(): NetworkResult<List<Track>> {
        return try {
            val response = api.getTracks()
            NetworkResult.Success(response.map { it.toTrack() })
        } catch (e: Exception) {
            NetworkResult.Error("Failed: ${e.message}")
        }
    }

    override suspend fun searchTracks(query: String): NetworkResult<List<Track>> {
        return try {
            val response = api.searchTracks(query)
            NetworkResult.Success(response.map { it.toTrack() })
        } catch (e: Exception) {
            NetworkResult.Error("Failed: ${e.message}")
        }
    }
}