package com.example.playlistmaker

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.media.MediaPlayer
import com.example.playlistmaker.data.TracksNetworkClient
import com.example.playlistmaker.data.network.TrackRetrofitTracksNetworkClient
import com.example.playlistmaker.data.repository.AudioPlayerRepositoryImpl
import com.example.playlistmaker.data.repository.SearchHistoryRepositoryImpl
import com.example.playlistmaker.data.repository.ThemeSaverRepositoryImpl
import com.example.playlistmaker.data.repository.TracksRepositoryImpl
import com.example.playlistmaker.domain.api.AudioPlayerInteractor
import com.example.playlistmaker.domain.api.GetTracksInteractor
import com.example.playlistmaker.domain.api.SearchHistoryInteractor
import com.example.playlistmaker.domain.api.ThemeSaverInteractor
import com.example.playlistmaker.domain.interactor.AudioPlayerInteractorImpl
import com.example.playlistmaker.domain.interactor.GetTracksInteractorImpl
import com.example.playlistmaker.domain.interactor.SearchHistoryInteractorImpl
import com.example.playlistmaker.domain.interactor.ThemeSaverInteractorImpl
import com.example.playlistmaker.domain.repository.AudioPlayerRepository
import com.example.playlistmaker.domain.repository.SearchHistoryRepository
import com.example.playlistmaker.domain.repository.ThemeSaverRepository
import com.example.playlistmaker.domain.repository.TracksRepository

object Creator {
    private const val SETTINGS_SHARED_PREFERENCES = "SETTINGS_SHARED_PREFERENCES"
    private const val SEARCH_HISTORY_SHARED_PREFERENCES = "SEARCH_HISTORY_SHARED_PREFERENCES"
    private lateinit var application: Application
    fun initApplication(application: Application) {
        this.application = application
    }

    fun provideSearchHistoryInteractor(): SearchHistoryInteractor {
        return SearchHistoryInteractorImpl(provideSearchHistoryRepository())
    }

    fun provideGetTracksInteractor(): GetTracksInteractor {
        return GetTracksInteractorImpl(provideTrackRepository())
    }

    fun provideAudioPlayerInteractor(): AudioPlayerInteractor {
        return AudioPlayerInteractorImpl(provideAudioPlayerRepository())
    }

    fun provideThemeSaverInteractor(): ThemeSaverInteractor {
        return ThemeSaverInteractorImpl(provideThemeSaverRepository())
    }

    private fun provideTrackRepository(): TracksRepository {
        return TracksRepositoryImpl(provideNetworkClient())
    }

    private fun provideSearchHistoryRepository(): SearchHistoryRepository {
        return SearchHistoryRepositoryImpl(provideSearchHistorySharedPrefs())
    }

    private fun provideAudioPlayerRepository(): AudioPlayerRepository {
        return AudioPlayerRepositoryImpl(provideMediaPlayer())
    }

    private fun provideThemeSaverRepository(): ThemeSaverRepository {
        return ThemeSaverRepositoryImpl(provideSettingsSharedPrefs())
    }

    private fun provideNetworkClient(): TracksNetworkClient {
        return TrackRetrofitTracksNetworkClient()
    }

    private fun provideSettingsSharedPrefs(): SharedPreferences {
        return application.getSharedPreferences(SETTINGS_SHARED_PREFERENCES, Context.MODE_PRIVATE)
    }

    private fun provideSearchHistorySharedPrefs(): SharedPreferences {
        return application.getSharedPreferences(
            SEARCH_HISTORY_SHARED_PREFERENCES,
            Context.MODE_PRIVATE
        )
    }

    private fun provideMediaPlayer(): MediaPlayer {
        return MediaPlayer()
    }
}