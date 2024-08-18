package com.example.playlistmaker.search.domain.api

import com.example.playlistmaker.search.domain.entity.Track

interface SearchHistoryInteractor {
    fun getTracks(): List<Track>
    fun saveTracks(tracks: List<Track>)
    fun addTrack(track: Track)
    fun clear()
}