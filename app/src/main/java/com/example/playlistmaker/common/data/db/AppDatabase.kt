package com.example.playlistmaker.common.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.playlistmaker.common.data.db.convertor.Converters
import com.example.playlistmaker.favourite.data.db.dao.FavouriteTracksDAO
import com.example.playlistmaker.favourite.data.db.entity.FavouriteTrackEntity
import com.example.playlistmaker.playlistLibrary.data.db.dao.PlaylistDAO
import com.example.playlistmaker.playlistLibrary.data.db.dao.TrackDao
import com.example.playlistmaker.playlistLibrary.data.db.entity.PlaylistEntity
import com.example.playlistmaker.playlistLibrary.data.db.entity.TrackEntity


@Database(
    version = 1,
    entities = [FavouriteTrackEntity::class, PlaylistEntity::class, TrackEntity::class]
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getFavouriteTracksDao(): FavouriteTracksDAO

    abstract fun getPlaylistDao(): PlaylistDAO

    abstract fun getTrackDao(): TrackDao
}