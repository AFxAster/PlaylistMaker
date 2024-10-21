package com.example.playlistmaker.library.data.repository

import com.example.playlistmaker.common.entity.Track
import com.example.playlistmaker.library.data.db.AppDatabase
import com.example.playlistmaker.library.data.mapper.toFavouriteTrackEntity
import com.example.playlistmaker.library.data.mapper.toTrack
import com.example.playlistmaker.library.domain.repository.FavouriteTracksRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class FavouriteTracksRepositoryImpl(
    private val database: AppDatabase
) : FavouriteTracksRepository {
    override fun addFavouriteTrack(track: Track) {
        database.getFavouriteTracksDao().insertFavouriteTrack(track.toFavouriteTrackEntity())
    }

    override fun deleteFavouriteTrack(track: Track) {
        database.getFavouriteTracksDao().deleteFavouriteTrack(track.toFavouriteTrackEntity())
    }

    override fun getFavouriteTracks(): Flow<List<Track>> = flow {
        val trackList = database.getFavouriteTracksDao().getFavouriteTracks().map { it.toTrack() }
        emit(trackList)
    }

    override fun getFlowableFavouriteTracks(): Flow<List<Track>> =
        database.getFavouriteTracksDao().getFlowableFavouriteTracks()
            .map { list -> list.map { it.toTrack() } }

    override fun getIsFavouriteTrackById(id: String): Flow<Boolean> = flow {
        val isFavourite = database.getFavouriteTracksDao().getIsFavouriteTrackById(id.toInt())
        emit(isFavourite)
    }
}