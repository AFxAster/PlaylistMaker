package com.example.playlistmaker.search.domain.repository

import com.example.playlistmaker.search.domain.entity.Track

interface TracksRepository {
    fun getTracks(query: String): List<Track>?
    fun getTrackById(id: String): Track?
}