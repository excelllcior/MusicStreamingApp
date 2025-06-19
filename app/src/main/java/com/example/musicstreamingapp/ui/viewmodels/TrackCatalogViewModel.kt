package com.example.musicstreamingapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicstreamingapp.domain.model.NetworkResult
import com.example.musicstreamingapp.domain.model.Track
import com.example.musicstreamingapp.domain.usecase.GetTracksUseCase
import com.example.musicstreamingapp.domain.usecase.SearchTracksUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class TrackCatalogEvent {
    object LoadTracks : TrackCatalogEvent()
    object RefreshTracks : TrackCatalogEvent()
    data class SearchTracks(val query: String) : TrackCatalogEvent()
}

data class TrackCatalogState(
    val tracks: List<Track> = emptyList(),
    val filteredTracks: List<Track> = emptyList(),
    val searchQuery: String = "",
    val isSearching: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

@HiltViewModel
class TrackCatalogViewModel @Inject constructor(
    private val getTracksUseCase: GetTracksUseCase,
    private val searchTracksUseCase: SearchTracksUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(TrackCatalogState())
    val state: StateFlow<TrackCatalogState> = _state.asStateFlow()

    init {
        loadTracks()
    }

    private fun loadTracks() {
        viewModelScope.launch {
            _state.update {
                it.copy(isLoading = true, errorMessage = null)
            }

            when (val result = getTracksUseCase()) {
                is NetworkResult.Success -> {
                    _state.update {
                        it.copy(
                            tracks = result.data ?: emptyList(),
                            isLoading = false
                        )
                    }
                }
                is NetworkResult.Error -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = result.message ?: "Unknown Error Occurred"
                        )
                    }
                }
                is NetworkResult.Loading -> {
                    _state.update { it.copy(isLoading = true) }
                }
            }
        }
    }

    private fun searchTracks(query: String) {
        viewModelScope.launch {
            if (query.isBlank()) {
                _state.update {
                    it.copy(
                        filteredTracks = it.tracks,
                        searchQuery = "",
                        isSearching = false
                    )
                }
            }

            when (val result = searchTracksUseCase(query)) {
                is NetworkResult.Success -> {
                    _state.update {
                        it.copy(
                            filteredTracks = result.data ?: emptyList(),
                            isSearching = false
                        )
                    }
                }
                is NetworkResult.Error -> {
                    _state.update {
                        it.copy(
                            isSearching = false,
                            errorMessage = result.message ?: "Search Error"
                        )
                    }
                }
                is NetworkResult.Loading -> {
                    _state.update { it.copy(isSearching = true) }
                }
            }
        }
    }

    fun onEvent(event: TrackCatalogEvent) {
        when (event) {
            is TrackCatalogEvent.LoadTracks -> loadTracks()
            is TrackCatalogEvent.RefreshTracks -> loadTracks()
            is TrackCatalogEvent.SearchTracks -> searchTracks(event.query)
        }
    }
}