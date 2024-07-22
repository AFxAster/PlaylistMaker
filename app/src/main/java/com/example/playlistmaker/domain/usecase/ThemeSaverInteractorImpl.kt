package com.example.playlistmaker.domain.usecase

import com.example.playlistmaker.domain.api.ThemeSaverInteractor
import com.example.playlistmaker.domain.repository.ThemeSaverRepository

class ThemeSaverInteractorImpl(private val themeSaverRepository: ThemeSaverRepository) :
    ThemeSaverInteractor {
    override fun isDarkThemeEnabled(): Boolean? = themeSaverRepository.isDarkThemeEnabled()

    override fun saveIsDarkTheme(isDark: Boolean) {
        themeSaverRepository.saveIsDarkTheme(isDark)
    }
}