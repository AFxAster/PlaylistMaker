package com.example.playlistmaker.search.data.repository

import com.example.playlistmaker.common.entity.Track
import com.example.playlistmaker.library.data.db.AppDatabase
import com.example.playlistmaker.search.data.TracksNetworkClient
import com.example.playlistmaker.search.data.dto.GetTrackByIdRequest
import com.example.playlistmaker.search.data.dto.GetTracksRequest
import com.example.playlistmaker.search.data.dto.ITunesResponse
import com.example.playlistmaker.search.data.mapper.toTrack
import com.example.playlistmaker.search.domain.repository.TracksRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

class TracksRepositoryImpl(
    private val tracksNetworkClient: TracksNetworkClient,
    private val database: AppDatabase
) : TracksRepository {

    override fun getTracks(query: String): Flow<List<Track>?> = flow {
        val response = tracksNetworkClient.getTracks(requestParams = GetTracksRequest(query))
        if (response.responseCode == 200 && response is ITunesResponse) {
            val favouriteTracksIds: List<String>
            withContext(Dispatchers.IO) {
                favouriteTracksIds =
                    database.getFavouriteTracksDao().getFavouriteTracks().map { it.trackId }
            }
            emit(response.results.map {
                it.toTrack().apply { isFavourite = favouriteTracksIds.contains(trackId) }
            })
        } else {
            emit(null)
        }
    }

    override fun getTrackById(id: String): Flow<Track?> = flow {
        val response = tracksNetworkClient.getTrackById(requestParams = GetTrackByIdRequest(id))
        if (response.responseCode == 200 && response is ITunesResponse) {
            val isFavourite: Boolean
            withContext(Dispatchers.IO) {
                isFavourite = database.getFavouriteTracksDao().getIsFavouriteTrackById(id.toInt())
            }
            emit(response.results.getOrNull(0)?.toTrack()?.apply {
                this.isFavourite = isFavourite
            })
        } else {
            emit(null)
        }
    }
}