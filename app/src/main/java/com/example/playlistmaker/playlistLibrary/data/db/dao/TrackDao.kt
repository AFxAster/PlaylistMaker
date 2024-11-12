package com.example.playlistmaker.playlistLibrary.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.playlistLibrary.data.db.entity.TrackEntity

@Dao
interface TrackDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTrack(track: TrackEntity)

    @Query("DELETE FROM track_table WHERE trackId = :arg0")
    fun deleteTrack(arg0: String)

    @Query("SELECT * FROM track_table WHERE trackId = :arg0")
    fun getTrackById(arg0: String): TrackEntity
}