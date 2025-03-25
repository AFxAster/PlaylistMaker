package com.example.playlistmaker.released.domain.repository

import com.example.playlistmaker.released.domain.entity.Artist

interface FollowedArtistsRepository {

    fun addFollowedArtist(artist: Artist)

    fun deleteFollowedArtist(artist: Artist)

    fun getFollowedArtists(): List<Artist>
}