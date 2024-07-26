package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.entity.Track

interface GetTracksInteractor {
    fun getTracks(query: String, consumer: TracksConsumer)
    fun getTrackById(id: String, consumer: TrackByIdConsumer)

    interface TracksConsumer {
        fun onTracksSuccess(tracks: List<Track>)
        fun onError()
    }

    interface TrackByIdConsumer {
        fun onTrackByIdSuccess(tracks: Track)
        fun onError()
    }
}