package com.example.playlistmaker.released.domain.entity

import java.util.Date

data class Release(
    val id: String,
    val releaseName: String,
    val artistName: String,
    val type: ReleaseType,
    val artworkUrl512: String,
    val releaseDate: Date,
    var tier: Int = 3
)