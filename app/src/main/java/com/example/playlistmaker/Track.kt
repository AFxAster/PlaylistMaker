package com.example.playlistmaker

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class Track(
    val trackId: String,
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: Long,
    val artworkUrl100: String,
    val releaseDate: Date,
    val primaryGenreName: String,
    val country: String,
    val previewUrl: String
) {
    val collectionName: String? = null
        get() = if (field?.endsWith(" - Single") == true)
            null
        else
            field

    fun getFormattedTime(pattern: String = "m:ss"): String =
        SimpleDateFormat(pattern, Locale.getDefault()).format(trackTimeMillis)

    fun getArtworkUrl512() = artworkUrl100.replaceAfterLast('/', "512x512bb.jpg")

    fun getReleaseYear(): String = SimpleDateFormat("Y", Locale.getDefault()).format(releaseDate)

}