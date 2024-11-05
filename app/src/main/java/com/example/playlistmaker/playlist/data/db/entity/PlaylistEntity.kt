package com.example.playlistmaker.playlist.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlist_table")
data class PlaylistEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val name: String,
    val description: String,
    val artworkPath: String?,
    val trackIds: List<String>,
    val tracksNumber: Int
)