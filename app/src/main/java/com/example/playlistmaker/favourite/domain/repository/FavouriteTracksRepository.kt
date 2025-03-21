package com.example.playlistmaker.favourite.domain.repository

import com.example.playlistmaker.common.entity.Track
import kotlinx.coroutines.flow.Flow

interface FavouriteTracksRepository {
    fun addFavouriteTrack(track: Track)
    fun deleteFavouriteTrack(track: Track)
    fun getFavouriteTracks(): Flow<List<Track>>
    fun getFlowableFavouriteTracks(): Flow<List<Track>>
    fun getIsFavouriteTrackById(id: String): Flow<Boolean>
}