package com.example.playlistmaker.released.domain.entity

sealed interface ReleaseType {
    data object Single : ReleaseType
    data object Album : ReleaseType
}