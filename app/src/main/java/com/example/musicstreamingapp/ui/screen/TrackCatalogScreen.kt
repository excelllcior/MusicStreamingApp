package com.example.musicstreamingapp.ui.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.musicstreamingapp.ui.viewmodels.TrackCatalogViewModel

@Composable
fun TrackCatalogScreen(
    viewModel: TrackCatalogViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
}