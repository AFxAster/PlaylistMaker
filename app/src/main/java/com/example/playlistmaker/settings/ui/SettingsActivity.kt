package com.example.playlistmaker.settings.ui

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.App
import com.example.playlistmaker.R
import com.example.playlistmaker.settings.presentation.viewmodel.SettingsViewModel
import com.google.android.material.switchmaterial.SwitchMaterial

class SettingsActivity : AppCompatActivity() {
    private val viewModel by lazy {
        ViewModelProvider(
            this,
            SettingsViewModel.getViewModelFactory()
        )[SettingsViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val backIcon: ImageView = findViewById(R.id.back_from_settings_button)
        backIcon.setOnClickListener {
            finish()
        }

        val shareButton: ImageView = findViewById(R.id.share_button)
        shareButton.setOnClickListener {
            viewModel.shareApp()
        }

        val supportButton: ImageView = findViewById(R.id.support_button)
        supportButton.setOnClickListener {
            viewModel.openSupport()
        }

        val userAgreementButton: ImageView = findViewById(R.id.user_agreement_button)
        userAgreementButton.setOnClickListener {
            viewModel.openTerms()
        }

        val themeSwitcher: SwitchMaterial = findViewById(R.id.theme_switcher)
        themeSwitcher.isChecked = viewModel.getIsDarkTheme().value ?: false
        themeSwitcher.setOnCheckedChangeListener { _, checked ->
            viewModel.setIsDarkTheme(checked)
        }
        viewModel.getIsDarkTheme().observe(this) {
            (applicationContext as App).switchTheme(it)
        }
    }
}