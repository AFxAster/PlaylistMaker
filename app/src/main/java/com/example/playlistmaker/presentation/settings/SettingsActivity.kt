package com.example.playlistmaker.presentation.settings

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.R
import com.example.playlistmaker.presentation.App
import com.google.android.material.switchmaterial.SwitchMaterial

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val backIcon: ImageView = findViewById(R.id.back_from_settings_button)
        backIcon.setOnClickListener {
            finish()
        }

        val shareButton: ImageView = findViewById(R.id.share_button)
        shareButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.url_android_course))
            startActivity(intent)
        }

        val supportButton: ImageView = findViewById(R.id.support_button)
        supportButton.setOnClickListener {
            val intent = Intent(
                Intent.ACTION_SENDTO,
                Uri.fromParts("mailto", getString(R.string.intent_mail_to), null)
            )
            intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.intent_mail_subject))
            intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.intent_mail_text))
            startActivity(intent)
        }

        val userAgreementButton: ImageView = findViewById(R.id.user_agreement_button)
        userAgreementButton.setOnClickListener {
            val intent =
                Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.url_user_agreement)))
            startActivity(intent)
        }

        val themeSwitcher: SwitchMaterial = findViewById(R.id.theme_switcher)
        themeSwitcher.isChecked =
            (applicationContext as App).isDarkThemeEnabled
        themeSwitcher.setOnCheckedChangeListener { _, checked ->
            (applicationContext as App).switchTheme(checked)
        }
    }
}