package com.example.playlistmaker.library.domain.api

import com.example.playlistmaker.common.entity.Track
import kotlinx.coroutines.flow.Flow

interface FavouriteTracksInteractor {
    fun addFavouriteTrack(track: Track)
    fun deleteFavouriteTrack(track: Track)
    fun getFavouriteTracks(): Flow<List<Track>>
    fun getFlowableFavouriteTracks(): Flow<List<Track>>
    fun getIsFavouriteTrackById(id: String): Flow<Boolean>
}