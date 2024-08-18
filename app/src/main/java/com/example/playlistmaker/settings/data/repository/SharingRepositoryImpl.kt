package com.example.playlistmaker.settings.data.repository

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.example.playlistmaker.settings.domain.entity.Email
import com.example.playlistmaker.settings.domain.repository.SharingRepository

class SharingRepositoryImpl(
    private val context: Context,
) : SharingRepository {
    override fun openLink(link: String) {
        val intent =
            Intent(Intent.ACTION_VIEW, Uri.parse(link))
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

    override fun shareLink(link: String) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, link)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

    override fun openEmail(email: Email) {
        val intent = Intent(
            Intent.ACTION_SENDTO,
            Uri.fromParts("mailto", email.to, null)
        )
        intent.putExtra(Intent.EXTRA_SUBJECT, email.subject)
        intent.putExtra(Intent.EXTRA_TEXT, email.message)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }
}