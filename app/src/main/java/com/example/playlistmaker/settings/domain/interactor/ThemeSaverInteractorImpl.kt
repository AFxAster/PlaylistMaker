package com.example.playlistmaker.settings.domain.interactor

import com.example.playlistmaker.settings.domain.api.ThemeSaverInteractor
import com.example.playlistmaker.settings.domain.repository.ThemeSaverRepository

class ThemeSaverInteractorImpl(private val themeSaverRepository: ThemeSaverRepository) :
    ThemeSaverInteractor {
    override fun isDarkThemeEnabled(): Boolean? = themeSaverRepository.isDarkThemeEnabled()

    override fun saveIsDarkTheme(isDark: Boolean) {
        themeSaverRepository.saveIsDarkTheme(isDark)
    }
}