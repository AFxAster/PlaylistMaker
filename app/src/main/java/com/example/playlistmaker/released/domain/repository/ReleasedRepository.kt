package com.example.playlistmaker.released.domain.repository

import com.example.playlistmaker.released.domain.entity.Release
import kotlinx.coroutines.flow.Flow
import java.util.Date

interface ReleasedRepository {
    fun getReleasedFrom(date: Date): Flow<List<Release>>
}