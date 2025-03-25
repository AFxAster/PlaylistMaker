package com.example.playlistmaker.released.domain.api

import com.example.playlistmaker.released.domain.entity.Artist

interface FollowedArtistsInteractor {

    fun addFollowedArtist(artist: Artist)

    fun deleteFollowedArtist(artist: Artist)

    fun getFollowedArtists(): List<Artist>
}