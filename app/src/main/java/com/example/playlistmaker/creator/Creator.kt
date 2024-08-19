package com.example.playlistmaker.creator

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.media.MediaPlayer
import com.example.playlistmaker.audioplayer.data.repository.AudioPlayerRepositoryImpl
import com.example.playlistmaker.audioplayer.domain.api.AudioPlayerInteractor
import com.example.playlistmaker.audioplayer.domain.interactor.AudioPlayerInteractorImpl
import com.example.playlistmaker.audioplayer.domain.repository.AudioPlayerRepository
import com.example.playlistmaker.search.data.TracksNetworkClient
import com.example.playlistmaker.search.data.network.TrackRetrofitTracksNetworkClient
import com.example.playlistmaker.search.data.repository.SearchHistoryRepositoryImpl
import com.example.playlistmaker.search.data.repository.TracksRepositoryImpl
import com.example.playlistmaker.search.domain.api.SearchHistoryInteractor
import com.example.playlistmaker.search.domain.api.TracksInteractor
import com.example.playlistmaker.search.domain.interactor.SearchHistoryInteractorImpl
import com.example.playlistmaker.search.domain.interactor.TracksInteractorImpl
import com.example.playlistmaker.search.domain.repository.SearchHistoryRepository
import com.example.playlistmaker.search.domain.repository.TracksRepository
import com.example.playlistmaker.settings.data.repository.LinkRepositoryImpl
import com.example.playlistmaker.settings.data.repository.SharingRepositoryImpl
import com.example.playlistmaker.settings.data.repository.ThemeSaverRepositoryImpl
import com.example.playlistmaker.settings.domain.api.SharingInteractor
import com.example.playlistmaker.settings.domain.api.ThemeSaverInteractor
import com.example.playlistmaker.settings.domain.interactor.SharingInteractorImpl
import com.example.playlistmaker.settings.domain.interactor.ThemeSaverInteractorImpl
import com.example.playlistmaker.settings.domain.repository.LinkRepository
import com.example.playlistmaker.settings.domain.repository.SharingRepository
import com.example.playlistmaker.settings.domain.repository.ThemeSaverRepository

object Creator {
    private const val SETTINGS_SHARED_PREFERENCES = "SETTINGS_SHARED_PREFERENCES"
    private const val SEARCH_HISTORY_SHARED_PREFERENCES = "SEARCH_HISTORY_SHARED_PREFERENCES"
    private lateinit var application: Application
    fun initApplication(application: Application) {
        Creator.application = application
    }

    fun provideSearchHistoryInteractor(): SearchHistoryInteractor {
        return SearchHistoryInteractorImpl(provideSearchHistoryRepository())
    }

    fun provideTracksInteractor(): TracksInteractor {
        return TracksInteractorImpl(provideTrackRepository())
    }

    fun provideAudioPlayerInteractor(): AudioPlayerInteractor {
        return AudioPlayerInteractorImpl(provideAudioPlayerRepository())
    }

    fun provideThemeSaverInteractor(): ThemeSaverInteractor {
        return ThemeSaverInteractorImpl(provideThemeSaverRepository())
    }

    fun provideSharingInteractor(): SharingInteractor {
        return SharingInteractorImpl(provideSharingRepository(), provideLinkRepository())
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

    private fun provideSharingRepository(): SharingRepository {
        return SharingRepositoryImpl(application)
    }

    private fun provideLinkRepository(): LinkRepository {
        return LinkRepositoryImpl(application)
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