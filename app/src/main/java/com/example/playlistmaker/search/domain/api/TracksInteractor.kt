package com.example.playlistmaker.search.domain.api

import com.example.playlistmaker.search.domain.entity.Track

interface TracksInteractor {
    fun getTracks(query: String, consumer: TracksConsumer)
    fun getTrackById(id: String, consumer: TrackByIdConsumer)

    interface TracksConsumer {
        fun onTracksSuccess(tracks: List<Track>)
        fun onError()
    }

    interface TrackByIdConsumer {
        fun onTrackByIdSuccess(track: Track)
        fun onError()
    }
}