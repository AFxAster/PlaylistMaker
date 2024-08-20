package com.example.playlistmaker.di

import com.example.playlistmaker.audioplayer.data.repository.AudioPlayerRepositoryImpl
import com.example.playlistmaker.audioplayer.domain.repository.AudioPlayerRepository
import com.example.playlistmaker.search.data.repository.SearchHistoryRepositoryImpl
import com.example.playlistmaker.search.data.repository.TracksRepositoryImpl
import com.example.playlistmaker.search.domain.repository.SearchHistoryRepository
import com.example.playlistmaker.search.domain.repository.TracksRepository
import com.example.playlistmaker.settings.data.repository.LinkRepositoryImpl
import com.example.playlistmaker.settings.data.repository.SharingRepositoryImpl
import com.example.playlistmaker.settings.data.repository.ThemeSaverRepositoryImpl
import com.example.playlistmaker.settings.domain.repository.LinkRepository
import com.example.playlistmaker.settings.domain.repository.SharingRepository
import com.example.playlistmaker.settings.domain.repository.ThemeSaverRepository
import org.koin.dsl.module

val repositoryModule = module {
    single<LinkRepository> {
        LinkRepositoryImpl(context = get())
    }

    single<SharingRepository> {
        SharingRepositoryImpl(context = get())
    }

    single<ThemeSaverRepository> {
        ThemeSaverRepositoryImpl(sharedPreferences = get())
    }

    single<SearchHistoryRepository> {
        SearchHistoryRepositoryImpl(sharedPreferences = get())
    }

    single<TracksRepository> {
        TracksRepositoryImpl(tracksNetworkClient = get())
    }

    factory<AudioPlayerRepository> {
        AudioPlayerRepositoryImpl(mediaPlayer = get())
    }
}
