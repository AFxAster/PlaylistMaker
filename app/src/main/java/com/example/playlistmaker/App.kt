package com.example.playlistmaker

import android.app.Application
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.creator.Creator

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        Creator.initApplication(this)
        val themeSaverInteractor = Creator.provideThemeSaverInteractor()
        val isSystemThemeDark =
            (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES

        val savedTheme = themeSaverInteractor.isDarkThemeEnabled()
        if (savedTheme == null)
            themeSaverInteractor.saveIsDarkTheme(isSystemThemeDark)

        switchTheme(savedTheme ?: isSystemThemeDark)
    }

    fun switchTheme(isDarkThemeEnabled: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (isDarkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }

}
