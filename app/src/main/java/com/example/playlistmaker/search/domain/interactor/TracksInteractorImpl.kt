package com.example.playlistmaker.search.domain.interactor

import com.example.playlistmaker.common.entity.Track
import com.example.playlistmaker.search.domain.api.TracksInteractor
import com.example.playlistmaker.search.domain.repository.TracksRepository
import kotlinx.coroutines.flow.Flow

class TracksInteractorImpl(private val tracksRepository: TracksRepository) : TracksInteractor {

    override fun getTracks(query: String): Flow<List<Track>?> =
        tracksRepository.getTracks(query)


    override fun getTrackById(id: String): Flow<Track?> =
        tracksRepository.getTrackById(id)
}