package com.example.playlistmaker.library.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.playlistmaker.library.data.db.convertor.Converters
import com.example.playlistmaker.library.data.db.dao.FavouriteTracksDAO
import com.example.playlistmaker.library.data.db.entity.FavouriteTrackEntity


@Database(version = 1, entities = [FavouriteTrackEntity::class])
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getFavouriteTracksDao(): FavouriteTracksDAO

}