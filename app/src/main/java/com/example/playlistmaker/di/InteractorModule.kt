package com.example.playlistmaker.di

import com.example.playlistmaker.audioplayer.domain.api.AudioPlayerInteractor
import com.example.playlistmaker.audioplayer.domain.interactor.AudioPlayerInteractorImpl
import com.example.playlistmaker.search.domain.api.SearchHistoryInteractor
import com.example.playlistmaker.search.domain.api.TracksInteractor
import com.example.playlistmaker.search.domain.interactor.SearchHistoryInteractorImpl
import com.example.playlistmaker.search.domain.interactor.TracksInteractorImpl
import com.example.playlistmaker.settings.domain.api.SharingInteractor
import com.example.playlistmaker.settings.domain.api.ThemeSaverInteractor
import com.example.playlistmaker.settings.domain.interactor.SharingInteractorImpl
import com.example.playlistmaker.settings.domain.interactor.ThemeSaverInteractorImpl
import org.koin.dsl.module

val interactorModule = module {
    single<SharingInteractor> {
        SharingInteractorImpl(sharingRepository = get(), linkRepository = get())
    }

    single<ThemeSaverInteractor> {
        ThemeSaverInteractorImpl(themeSaverRepository = get())
    }

    single<SearchHistoryInteractor> {
        SearchHistoryInteractorImpl(historyRepository = get())
    }

    single<TracksInteractor> {
        TracksInteractorImpl(tracksRepository = get())
    }

    factory<AudioPlayerInteractor> {
        AudioPlayerInteractorImpl(audioPlayerRepository = get())
    }
}
