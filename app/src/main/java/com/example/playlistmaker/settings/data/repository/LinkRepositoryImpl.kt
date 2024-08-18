package com.example.playlistmaker.settings.data.repository

import android.content.Context
import com.example.playlistmaker.R
import com.example.playlistmaker.settings.domain.entity.Email
import com.example.playlistmaker.settings.domain.repository.LinkRepository

class LinkRepositoryImpl(private val context: Context) : LinkRepository {
    override fun getShareAppLink(): String {
        return context.getString(R.string.url_android_course)
    }

    override fun getSupportEmail(): Email {
        return Email(
            to = context.getString(R.string.intent_mail_to),
            subject = context.getString(R.string.intent_mail_subject),
            message = context.getString(R.string.intent_mail_text)
        )
    }

    override fun getTermsLink(): String {
        return context.getString(R.string.url_user_agreement)
    }
}