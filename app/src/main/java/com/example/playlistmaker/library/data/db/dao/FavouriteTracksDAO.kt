package com.example.playlistmaker.library.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.library.data.db.entity.FavouriteTrackEntity

@Dao
interface FavouriteTracksDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFavouriteTrack(track: FavouriteTrackEntity)

    @Delete
    fun deleteFavouriteTrack(track: FavouriteTrackEntity)

    @Query("SELECT * FROM favourite_table")
    fun getFavouriteTracks(): List<FavouriteTrackEntity>

    @Query("SELECT 1 FROM favourite_table WHERE trackId = :id")
    fun getIsFavouriteTrackById(id: Int): Boolean
}