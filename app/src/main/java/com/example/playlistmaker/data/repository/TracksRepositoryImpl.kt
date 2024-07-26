package com.example.playlistmaker.data.repository

import com.example.playlistmaker.data.TracksNetworkClient
import com.example.playlistmaker.data.dto.GetTrackByIdRequest
import com.example.playlistmaker.data.dto.GetTracksRequest
import com.example.playlistmaker.data.dto.ITunesResponse
import com.example.playlistmaker.data.mapper.toTrack
import com.example.playlistmaker.domain.entity.Track
import com.example.playlistmaker.domain.repository.TracksRepository

class TracksRepositoryImpl(private val tracksNetworkClient: TracksNetworkClient): TracksRepository {
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