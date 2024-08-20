package com.example.playlistmaker.settings.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.App
import com.example.playlistmaker.databinding.ActivitySettingsBinding
import com.example.playlistmaker.settings.presentation.viewmodel.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backFromSettingsButton.setOnClickListener {
            finish()
        }

        val viewModel: SettingsViewModel by viewModel()


        binding.shareButton.setOnClickListener {
            viewModel.shareApp()
        }

        binding.supportButton.setOnClickListener {
            viewModel.openSupport()
        }

        binding.userAgreementButton.setOnClickListener {
            viewModel.openTerms()
        }

        binding.themeSwitcher.isChecked = viewModel.getIsDarkTheme().value ?: false
        binding.themeSwitcher.setOnCheckedChangeListener { _, checked ->
            viewModel.setIsDarkTheme(checked)
        }


        viewModel.getIsDarkTheme().observe(this) {
            (applicationContext as App).switchTheme(it)
        }
    }
}