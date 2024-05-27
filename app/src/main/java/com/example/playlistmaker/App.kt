package com.example.playlistmaker

import android.app.Application
import android.content.SharedPreferences
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate

class App : Application() {
    private val SETTINGS_SHARED_PREFERENCES = "SETTINGS_SHARED_PREFERENCES"
    private val DARK_THEME_KEY = "DARK_THEME_KEY"
    private lateinit var sharedPrefs: SharedPreferences
    var darkThemeEnabled = false
    override fun onCreate() {
        super.onCreate()
        sharedPrefs = getSharedPreferences(SETTINGS_SHARED_PREFERENCES, MODE_PRIVATE)
        switchTheme(
            sharedPrefs.getBoolean(
                DARK_THEME_KEY,
                (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES
            )
        )
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        this.darkThemeEnabled = darkThemeEnabled
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
        sharedPrefs.edit().putBoolean(DARK_THEME_KEY, darkThemeEnabled).apply()
    }

}
