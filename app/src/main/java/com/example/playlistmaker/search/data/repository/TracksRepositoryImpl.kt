package com.example.playlistmaker.search.data.repository

import com.example.playlistmaker.search.data.TracksNetworkClient
import com.example.playlistmaker.search.data.dto.GetTrackByIdRequest
import com.example.playlistmaker.search.data.dto.GetTracksRequest
import com.example.playlistmaker.search.data.dto.ITunesResponse
import com.example.playlistmaker.search.data.mapper.toTrack
import com.example.playlistmaker.search.domain.entity.Track
import com.example.playlistmaker.search.domain.repository.TracksRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TracksRepositoryImpl(private val tracksNetworkClient: TracksNetworkClient) :
    TracksRepository {

    override suspend fun getTracks(query: String): Flow<List<Track>?> = flow {
        val response = tracksNetworkClient.getTracks(requestParams = GetTracksRequest(query))
        if (response.responseCode == 200 && response is ITunesResponse) {
            emit(response.results.map { it.toTrack() })
        } else {
            emit(null)
        }
    }

    override suspend fun getTrackById(id: String): Flow<Track?> = flow {
        val response = tracksNetworkClient.getTrackById(requestParams = GetTrackByIdRequest(id))
        if (response.responseCode == 200 && response is ITunesResponse) {
            emit(response.results.getOrNull(0)?.toTrack())
        } else {
            emit(null)
        }
    }
}