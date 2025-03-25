package com.example.playlistmaker.released.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "followed_artist_table")
data class FollowedArtistEntity(
    @PrimaryKey
    val id: String,
    val name: String
)