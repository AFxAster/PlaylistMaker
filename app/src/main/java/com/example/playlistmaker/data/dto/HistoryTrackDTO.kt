package com.example.playlistmaker.data.dto

import java.util.Date

data class HistoryTrackDTO(
    val trackId: String,
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: Long,
    val artworkUrl100: String,
    val releaseDate: Date,
    val primaryGenreName: String,
    val country: String,
    val previewUrl: String,
    val collectionName: String?
)