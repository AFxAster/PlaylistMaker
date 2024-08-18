package com.example.playlistmaker.search.domain.repository

import com.example.playlistmaker.search.domain.entity.Track

interface SearchHistoryRepository {
    fun getTracks(): List<Track>
    fun saveTracks(tracks: List<Track>)
    fun clear()
}