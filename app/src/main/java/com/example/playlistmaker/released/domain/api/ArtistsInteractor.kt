package com.example.playlistmaker.released.domain.api

import com.example.playlistmaker.released.domain.entity.Artist
import kotlinx.coroutines.flow.Flow

interface ArtistsInteractor {

    fun getArtists(query: String): Flow<List<Artist>?>

    fun followArtist(artist: Artist)

    fun unfollowArtist(artist: Artist)

    fun getFollowedArtists(query: String): Flow<List<Artist>?>
}