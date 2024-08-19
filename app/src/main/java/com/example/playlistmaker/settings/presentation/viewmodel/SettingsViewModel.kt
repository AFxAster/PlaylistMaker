package com.example.playlistmaker.settings.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
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
}