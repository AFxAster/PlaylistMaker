package com.example.playlistmaker.released.domain.entity

import java.util.Date

data class Release(
    val id: String,
    val releaseName: String,
    val artistName: String,
    val type: ReleaseType,
    val artworkUrl100: String,
    val releaseDate: Date
)