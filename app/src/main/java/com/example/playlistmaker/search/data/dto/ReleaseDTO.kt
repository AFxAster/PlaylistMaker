package com.example.playlistmaker.search.data.dto

import java.util.Date

data class ReleaseDTO(
    val artistId: String,
    val collectionId: String,
    val artistName: String,
    val collectionName: String,
    val artworkUrl100: String, // todo мб 512 надо
    val releaseDate: Date
)