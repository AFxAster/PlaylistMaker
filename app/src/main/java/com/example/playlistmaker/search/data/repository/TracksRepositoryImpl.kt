package com.example.playlistmaker.search.data.repository

import com.example.playlistmaker.search.data.TracksNetworkClient
import com.example.playlistmaker.search.data.dto.GetTrackByIdRequest
import com.example.playlistmaker.search.data.dto.GetTracksRequest
import com.example.playlistmaker.search.data.dto.ITunesResponse
import com.example.playlistmaker.search.data.mapper.toTrack
import com.example.playlistmaker.search.domain.entity.Track
import com.example.playlistmaker.search.domain.repository.TracksRepository

class TracksRepositoryImpl(private val tracksNetworkClient: TracksNetworkClient) :
    TracksRepository {
    override fun getTracks(query: String): List<Track>? {
        val response = tracksNetworkClient.getTracks(requestParams = GetTracksRequest(query))
        return if (response.responseCode == 200 && response is ITunesResponse) {
            response.results.map { it.toTrack() }
        } else {
            null
        }
    }

    override fun getTrackById(id: String): Track? {
        val response = tracksNetworkClient.getTrackById(requestParams = GetTrackByIdRequest(id))
        return if (response.responseCode == 200 && response is ITunesResponse) {
            response.results.getOrNull(0)?.toTrack()
        } else {
            null
        }
    }
}