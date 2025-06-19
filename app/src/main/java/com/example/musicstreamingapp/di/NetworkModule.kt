package com.example.musicstreamingapp.di

import com.example.musicstreamingapp.data.datasource.remote.api.TrackApi
import com.example.musicstreamingapp.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("${Constants.SERVER_URL}/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideTrackApi(retrofit: Retrofit): TrackApi = retrofit.create(TrackApi::class.java)
}