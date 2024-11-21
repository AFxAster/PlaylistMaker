package com.example.playlistmaker.playlistLibrary.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.playlistmaker.playlistLibrary.data.db.entity.PlaylistEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaylistDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPlaylist(playlist: PlaylistEntity): Long

    @Update
    fun updatePlaylist(playlist: PlaylistEntity)

    @Query("DELETE FROM playlist_table WHERE id = :arg0")
    fun deletePlaylist(arg0: Long)

    @Query("SELECT * FROM playlist_table")
    fun getPlaylists(): List<PlaylistEntity>

    @Query("SELECT * FROM playlist_table")
    fun getFlowablePlaylists(): Flow<List<PlaylistEntity>>

    @Query("SELECT * FROM playlist_table WHERE id = :arg0")
    fun getPlaylistById(arg0: Long): PlaylistEntity

    @Query("SELECT * FROM playlist_table WHERE id = :arg0")
    fun getFlowablePlaylistById(arg0: Long): Flow<PlaylistEntity?>
}