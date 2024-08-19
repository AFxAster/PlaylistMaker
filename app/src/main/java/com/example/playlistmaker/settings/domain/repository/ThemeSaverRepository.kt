package com.example.playlistmaker.settings.domain.repository

interface ThemeSaverRepository {
    fun isDarkThemeEnabled(): Boolean?

    fun saveIsDarkTheme(isDark: Boolean)
}