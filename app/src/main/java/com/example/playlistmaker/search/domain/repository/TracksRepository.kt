package com.example.playlistmaker.search.domain.repository

import com.example.playlistmaker.common.entity.Track
import kotlinx.coroutines.flow.Flow

interface TracksRepository {
    fun getTracks(query: String): Flow<List<Track>?>
    fun getTrackById(id: String): Flow<Track?>
}