package com.example.playlistmaker.search.domain.repository

import com.example.playlistmaker.common.entity.Track

interface SearchHistoryRepository {
    fun getTracks(): List<Track>
    fun saveTracks(tracks: List<Track>)
    fun clear()
}