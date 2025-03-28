package com.example.playlistmaker.released.domain.repository

import com.example.playlistmaker.released.domain.entity.Artist
import kotlinx.coroutines.flow.Flow

interface ArtistsRepository {

    fun getArtists(query: String): Flow<List<Artist>?>
}