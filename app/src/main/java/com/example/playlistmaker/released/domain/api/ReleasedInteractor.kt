package com.example.playlistmaker.released.domain.api

import com.example.playlistmaker.released.domain.entity.Release
import kotlinx.coroutines.flow.Flow
import java.util.Calendar

interface ReleasedInteractor {
    fun getReleases(from: Calendar?, to: Calendar?): Flow<List<Release>?>
}