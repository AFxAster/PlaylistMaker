package com.example.playlistmaker.released.domain.repository

import com.example.playlistmaker.released.domain.entity.Artist
import com.example.playlistmaker.released.domain.entity.Release
import kotlinx.coroutines.flow.Flow
import java.util.Calendar

interface ReleasedRepository {
    fun getReleases(artist: Artist, from: Calendar?, to: Calendar?): Flow<List<Release>?>
}