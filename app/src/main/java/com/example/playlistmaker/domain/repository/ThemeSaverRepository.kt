package com.example.playlistmaker.domain.repository

interface ThemeSaverRepository {
    fun isDarkThemeEnabled(): Boolean?

    fun saveIsDarkTheme(isDark: Boolean)
}