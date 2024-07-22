package com.example.playlistmaker.domain.api

interface ThemeSaverInteractor {
    fun isDarkThemeEnabled(): Boolean?
    fun saveIsDarkTheme(isDark: Boolean)
}