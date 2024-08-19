package com.example.playlistmaker.search.domain.interactor

import com.example.playlistmaker.search.domain.api.TracksInteractor
import com.example.playlistmaker.search.domain.repository.TracksRepository
import java.util.concurrent.Executors

class TracksInteractorImpl(private val tracksRepository: TracksRepository) : TracksInteractor {
    private val executor = Executors.newCachedThreadPool()
    override fun getTracks(query: String, consumer: TracksInteractor.TracksConsumer) {
        if (query.isBlank()) return
        executor.execute {
            val tracks = tracksRepository.getTracks(query)
            if (tracks != null) {
                consumer.onTracksSuccess(tracks)
            } else {
                consumer.onError()
            }
        }
    }

    override fun getTrackById(id: String, consumer: TracksInteractor.TrackByIdConsumer) {
        executor.execute {
            val track = tracksRepository.getTrackById(id)
            if (track != null) {
                consumer.onTrackByIdSuccess(track)
            } else {
                consumer.onError()
            }
        }
    }
}