package com.example.playlistmaker.released.domain.interactor

import com.example.playlistmaker.released.domain.api.ArtistsInteractor
import com.example.playlistmaker.released.domain.entity.Artist
import com.example.playlistmaker.released.domain.repository.ArtistsRepository
import com.example.playlistmaker.released.domain.repository.FollowedArtistsRepository
import kotlinx.coroutines.flow.Flow

class ArtistsInteractorImpl(
    private val artistsRepository: ArtistsRepository,
    private val followedArtistsRepository: FollowedArtistsRepository
) : ArtistsInteractor {

    override fun getArtists(query: String): Flow<List<Artist>?> {
        return artistsRepository.getArtists(query)
    }

    override fun followArtist(artist: Artist) {
        followedArtistsRepository.followArtist(artist)
    }

    override fun unfollowArtist(artist: Artist) {
        followedArtistsRepository.unfollowArtist(artist)
    }

    override fun getFollowedArtists(query: String): Flow<List<Artist>?> {
        return followedArtistsRepository.getFollowedArtists()
    }
}