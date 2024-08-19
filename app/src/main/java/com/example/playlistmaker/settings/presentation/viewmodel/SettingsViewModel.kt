package com.example.playlistmaker.settings.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.settings.domain.api.SharingInteractor
import com.example.playlistmaker.settings.domain.api.ThemeSaverInteractor

class SettingsViewModel(
    private val sharingInteractor: SharingInteractor,
    private val themeSaverInteractor: ThemeSaverInteractor
) : ViewModel() {

    private val isDarkTheme: MutableLiveData<Boolean> =
        MutableLiveData(themeSaverInteractor.isDarkThemeEnabled())

    fun getIsDarkTheme(): LiveData<Boolean> = isDarkTheme

    fun setIsDarkTheme(isDark: Boolean) {
        isDarkTheme.value = isDark
        themeSaverInteractor.saveIsDarkTheme(isDark)
    }

    fun shareApp() {
        sharingInteractor.shareApp()
    }

    fun openTerms() {
        sharingInteractor.openTerms()
    }

    fun openSupport() {
        sharingInteractor.openSupport()
    }

    companion object {
        fun getViewModelFactory(): ViewModelProvider.Factory {
            return viewModelFactory {
                initializer {
                    SettingsViewModel(
                        Creator.provideSharingInteractor(),
                        Creator.provideThemeSaverInteractor()
                    )
                }
            }
        }
    }
}