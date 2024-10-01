package com.example.playlistmaker.audioplayer.domain.api

interface StatusObserver {
    fun onPrepared()
    fun onPlay(position: Int)
    fun onPause(position: Int)
    fun onCompletion()
}