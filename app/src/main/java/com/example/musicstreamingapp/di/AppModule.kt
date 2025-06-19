package com.example.musicstreamingapp.di

import android.content.Context
import com.example.musicstreamingapp.data.datasource.remote.api.TrackApi
import com.example.musicstreamingapp.data.repository.TrackRepositoryImpl
import com.example.musicstreamingapp.domain.repository.TrackRepository
import com.example.musicstreamingapp.domain.usecase.GetTracksUseCase
import com.example.musicstreamingapp.ui.service.AudioPlayer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    fun provideTrackRepository(trackApi: TrackApi): TrackRepository =
        TrackRepositoryImpl(trackApi)

    @Provides
    fun provideGetTracksUseCase(repository: TrackRepository): GetTracksUseCase =
        GetTracksUseCase(repository)

    @Provides
    @Singleton
    fun provideAudioPlayer(@ApplicationContext context: Context): AudioPlayer {
        return AudioPlayer(context)
    }
}