package com.example.playlistmaker.released.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ArtistDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFollowedArtist(artist: FollowedArtistEntity)

    @Delete
    fun deleteFollowedArtist(artist: FollowedArtistEntity)

    @Query("SELECT * FROM followed_artist_table")
    fun getFollowedArtists(): List<FollowedArtistEntity>
}