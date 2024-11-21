package com.example.playlistmaker.settings.domain.repository

import com.example.playlistmaker.settings.domain.entity.Email

interface SharingRepository {
    fun openLink(link: String)
    fun shareText(text: String)
    fun openEmail(email: Email)
}