package com.example.playlistmaker.ui

import android.app.Application
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.Creator
import com.example.playlistmaker.domain.api.ThemeSaverInteractor

class App : Application() {
    var isDarkThemeEnabled = false
    private lateinit var themeSaverInteractor: ThemeSaverInteractor

    override fun onCreate() {
        super.onCreate()
        Creator.initApplication(this)
        themeSaverInteractor = Creator.provideThemeSaverInteractor()
        val isSystemThemeDark =
            (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES
        switchTheme(themeSaverInteractor.isDarkThemeEnabled() ?: isSystemThemeDark)
    }

    fun switchTheme(isDarkThemeEnabled: Boolean) {
        this.isDarkThemeEnabled = isDarkThemeEnabled
        AppCompatDelegate.setDefaultNightMode(
            if (isDarkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
        themeSaverInteractor.saveIsDarkTheme(isDarkThemeEnabled)
    }

}
