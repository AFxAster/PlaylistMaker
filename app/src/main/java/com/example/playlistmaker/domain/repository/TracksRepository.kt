package com.example.playlistmaker.domain.repository

import com.example.playlistmaker.domain.entity.Track

interface TracksRepository {
    fun getTracks(query: String): List<Track>?
    fun getTrackById(id: String): Track?
}