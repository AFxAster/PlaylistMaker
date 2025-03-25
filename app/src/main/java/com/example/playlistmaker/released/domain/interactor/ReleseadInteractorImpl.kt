package com.example.playlistmaker.released.domain.interactor

import com.example.playlistmaker.released.domain.api.ReleasedInteractor
import com.example.playlistmaker.released.domain.entity.Release
import com.example.playlistmaker.released.domain.repository.ReleasedRepository
import kotlinx.coroutines.flow.Flow
import java.util.Date

class ReleseadInteractorImpl(
    private val releasedRepository: ReleasedRepository
) : ReleasedInteractor {
    override fun getReleasedFrom(date: Date): Flow<List<Release>?> {
        return releasedRepository.getReleasedFrom(date)
    }
}