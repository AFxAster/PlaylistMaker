package com.example.playlistmaker.domain.interactor

import com.example.playlistmaker.domain.api.GetTracksInteractor
import com.example.playlistmaker.domain.repository.TracksRepository
import java.util.concurrent.Executors

class GetTracksInteractorImpl(private val tracksRepository: TracksRepository): GetTracksInteractor {
    private val executor = Executors.newCachedThreadPool()
    override fun getTracks(query: String, consumer: GetTracksInteractor.TracksConsumer) {
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

    override fun getTrackById(id: String, consumer: GetTracksInteractor.TrackByIdConsumer) {
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