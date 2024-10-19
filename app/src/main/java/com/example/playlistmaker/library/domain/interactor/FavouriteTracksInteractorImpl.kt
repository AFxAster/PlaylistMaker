package com.example.playlistmaker.library.domain.interactor

import com.example.playlistmaker.common.entity.Track
import com.example.playlistmaker.library.domain.api.FavouriteTracksInteractor
import com.example.playlistmaker.library.domain.repository.FavouriteTracksRepository
import kotlinx.coroutines.flow.Flow

class FavouriteTracksInteractorImpl(
    private val favouriteTracksRepository: FavouriteTracksRepository
) : FavouriteTracksInteractor {
    override fun addFavouriteTrack(track: Track) {
        favouriteTracksRepository.addFavouriteTrack(track)
    }

    override fun deleteFavouriteTrack(track: Track) {
        favouriteTracksRepository.deleteFavouriteTrack(track)
    }

    override fun getFavouriteTracks(): Flow<List<Track>> =
        favouriteTracksRepository.getFavouriteTracks()

    override fun getIsFavouriteTrackById(id: String): Flow<Boolean> =
        favouriteTracksRepository.getIsFavouriteTrackById(id)
}