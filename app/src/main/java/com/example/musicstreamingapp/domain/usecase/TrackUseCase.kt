package com.example.musicstreamingapp.domain.usecase

import com.example.musicstreamingapp.domain.model.NetworkResult
import com.example.musicstreamingapp.domain.model.Track
import com.example.musicstreamingapp.domain.repository.TrackRepository
import javax.inject.Inject

class GetTracksUseCase @Inject constructor(
    private val repository: TrackRepository
) {
    suspend operator fun invoke(): NetworkResult<List<Track>> {
        return repository.getTracks()
    }
}

class SearchTracksUseCase @Inject constructor(
    private val repository: TrackRepository
) {
    suspend operator fun invoke(query: String): NetworkResult<List<Track>> {
        return repository.searchTracks(query)
    }
}