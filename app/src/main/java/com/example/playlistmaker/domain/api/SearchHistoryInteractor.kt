package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.entity.Track

interface SearchHistoryInteractor {
    fun getTracks(): List<Track>
    fun saveTracks(tracks: List<Track>)
    fun addTrack(track: Track)
    fun clear()
}