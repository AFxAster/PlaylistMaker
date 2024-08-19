package com.example.playlistmaker.settings.domain.api

interface ThemeSaverInteractor {
    fun isDarkThemeEnabled(): Boolean?
    fun saveIsDarkTheme(isDark: Boolean)
}