package com.example.playlistmaker.settings.domain.repository

import com.example.playlistmaker.settings.domain.entity.Email

interface LinkRepository {
    fun getShareAppLink(): String
    fun getSupportEmail(): Email
    fun getTermsLink(): String
}