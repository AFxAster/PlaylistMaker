package com.example.playlistmaker.released.domain.interactor

import com.example.playlistmaker.released.domain.api.FollowedArtistsInteractor
import com.example.playlistmaker.released.domain.entity.Artist
import com.example.playlistmaker.released.domain.repository.FollowedArtistsRepository

class FollowedArtistsInteractorImpl(
    private val followedArtistsRepository: FollowedArtistsRepository
) : FollowedArtistsInteractor {
    override fun addFollowedArtist(artist: Artist) {
        followedArtistsRepository.addFollowedArtist(artist)
    }

    override fun deleteFollowedArtist(artist: Artist) {
        followedArtistsRepository.deleteFollowedArtist(artist)
    }

    override fun getFollowedArtists(): List<Artist> = followedArtistsRepository.getFollowedArtists()
}