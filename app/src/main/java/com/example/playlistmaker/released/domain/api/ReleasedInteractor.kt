package com.example.playlistmaker.released.domain.api

import com.example.playlistmaker.released.domain.entity.Release
import kotlinx.coroutines.flow.Flow
import java.util.Date

interface ReleasedInteractor {
    fun getReleasesFrom(date: Date): Flow<List<Release>?>
}