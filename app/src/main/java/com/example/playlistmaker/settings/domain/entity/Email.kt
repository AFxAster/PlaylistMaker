package com.example.playlistmaker.settings.domain.entity

data class Email(
    val to: String,
    val subject: String,
    val message: String
)