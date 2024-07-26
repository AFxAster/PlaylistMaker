package com.example.playlistmaker.domain.repository

import com.example.playlistmaker.domain.entity.Track

interface SearchHistoryRepository {
    fun getTracks(): List<Track>
    fun saveTracks(tracks: List<Track>)
    fun clear()
}