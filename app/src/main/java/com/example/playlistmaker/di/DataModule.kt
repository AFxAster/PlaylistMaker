package com.example.playlistmaker.di

import android.content.Context
import android.content.SharedPreferences
import android.media.MediaPlayer
import androidx.room.Room
import com.example.playlistmaker.common.data.db.AppDatabase
import com.example.playlistmaker.search.data.TracksNetworkClient
import com.example.playlistmaker.search.data.network.ITunesApi
import com.example.playlistmaker.search.data.network.TrackRetrofitTracksNetworkClient
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val dataModule = module {
    single<TracksNetworkClient> {
        TrackRetrofitTracksNetworkClient(api = get())
    }

    single<ITunesApi> {
        Retrofit.Builder()
            .baseUrl("https://itunes.apple.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ITunesApi::class.java)
    }

    single<SharedPreferences> {
        androidContext().getSharedPreferences("STORAGE_SHARED_PREFERENCES", Context.MODE_PRIVATE)
    }

    factory<MediaPlayer> {
        MediaPlayer()
    }

    single<AppDatabase> {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, "database.db")
            .build()

    }
}
