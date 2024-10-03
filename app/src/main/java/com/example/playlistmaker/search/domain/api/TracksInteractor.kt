package com.example.playlistmaker.search.domain.api

import com.example.playlistmaker.search.domain.entity.Track
import kotlinx.coroutines.flow.Flow

interface TracksInteractor {
    fun getTracks(query: String): Flow<List<Track>?>
    fun getTrackById(id: String): Flow<Track?>
}