package com.example.playlistmaker.released.domain.repository

import com.example.playlistmaker.released.domain.entity.Artist
import kotlinx.coroutines.flow.Flow

interface FollowedArtistsRepository {

    fun followArtist(artist: Artist)

    fun unfollowArtist(artist: Artist)

    fun getFollowedArtists(): Flow<List<Artist>?>
}