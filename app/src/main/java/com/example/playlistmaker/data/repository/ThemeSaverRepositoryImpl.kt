package com.example.playlistmaker.data.repository

import android.content.SharedPreferences
import com.example.playlistmaker.domain.repository.ThemeSaverRepository

class ThemeSaverRepositoryImpl(private val sharedPreferences: SharedPreferences) :
    ThemeSaverRepository {
    override fun isDarkThemeEnabled(): Boolean? {
        return if (!sharedPreferences.contains(DARK_THEME_KEY)) null
        else
            sharedPreferences.getBoolean(
                DARK_THEME_KEY,
                false
            )
    }

    override fun saveIsDarkTheme(isDark: Boolean) {
        sharedPreferences.edit().putBoolean(DARK_THEME_KEY, isDark).apply()
    }

    private companion object {
        const val DARK_THEME_KEY = "DARK_THEME_KEY"
    }
}