package com.example.playlistmaker.released.data.repository

import com.example.playlistmaker.released.data.dto.ArtistsResponse
import com.example.playlistmaker.released.data.dto.GetArtistsRequest
import com.example.playlistmaker.released.data.toArtist
import com.example.playlistmaker.released.domain.entity.Artist
import com.example.playlistmaker.released.domain.repository.ArtistsRepository
import com.example.playlistmaker.search.data.ITunesNetworkClient
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ArtistsRepositoryImpl(
    private val networkClient: ITunesNetworkClient
) : ArtistsRepository {
    override fun getArtists(query: String): Flow<List<Artist>?> = flow {
        val response = networkClient.getArtists(GetArtistsRequest(query))
        if (response.responseCode == 200 && response is ArtistsResponse) {
            emit(response.results.map { it.toArtist() })
        } else {
            emit(null)
        }
    }
}