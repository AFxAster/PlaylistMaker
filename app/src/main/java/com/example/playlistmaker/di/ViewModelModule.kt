package com.example.playlistmaker.di

import com.example.playlistmaker.audioplayer.presentation.viewmodel.AudioPlayerViewModel
import com.example.playlistmaker.editplaylist.presentation.viewmodel.EditPlaylistViewModel
import com.example.playlistmaker.favourite.presentation.viewmodel.FavouriteViewModel
import com.example.playlistmaker.newplaylist.presentation.viewmodel.NewPlaylistViewModel
import com.example.playlistmaker.playlist.presentation.viewmodel.PlaylistViewModel
import com.example.playlistmaker.playlistLibrary.presentation.viewmodel.PlaylistLibraryViewModel
import com.example.playlistmaker.playlistmenu.presentation.viewmodel.PlaylistMenuViewModel
import com.example.playlistmaker.playlistsbottomsheet.presentation.viewmodel.PlaylistsBottomSheetViewModel
import com.example.playlistmaker.search.presentation.viewmodel.SearchViewModel
import com.example.playlistmaker.settings.presentation.viewmodel.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel<SettingsViewModel> {
        SettingsViewModel(sharingInteractor = get(), themeSaverInteractor = get())
    }

    viewModel<SearchViewModel> {
        SearchViewModel(tracksInteractor = get(), searchHistoryInteractor = get())
    }

    viewModel<AudioPlayerViewModel> { params ->
        AudioPlayerViewModel(
            trackId = params.get(),
            tracksInteractor = get(),
            audioPlayerInteractor = get(),
            favouriteTracksInteractor = get()
        )
    }

    viewModel<FavouriteViewModel> {
        FavouriteViewModel(favouriteTracksInteractor = get())
    }

    viewModel<PlaylistLibraryViewModel> {
        PlaylistLibraryViewModel(playlistInteractor = get())
    }

    viewModel<NewPlaylistViewModel> {
        NewPlaylistViewModel(playlistInteractor = get(), imageInteractor = get())
    }

    viewModel<PlaylistsBottomSheetViewModel> { params ->
        PlaylistsBottomSheetViewModel(
            trackId = params.get(),
            playlistInteractor = get(),
            tracksInteractor = get()
        )
    }

    viewModel<PlaylistViewModel> { params ->
        PlaylistViewModel(
            id = params.get(),
            playlistInteractor = get(),
            sharingInteractor = get()
        )
    }

    viewModel<PlaylistMenuViewModel> { params ->
        PlaylistMenuViewModel(
            id = params.get(),
            playlistInteractor = get(),
            sharingInteractor = get()
        )
    }

    viewModel<EditPlaylistViewModel> { params ->
        EditPlaylistViewModel(
            id = params.get(),
            playlistInteractor = get(),
            imageInteractor = get()
        )
    }
}
