package com.example.playlistmaker.domain.api

interface AudioPlayerInteractor {
    fun initWithSource(src: String, onPrepared: (() -> Unit)?)
    fun setOnCompletionListener(onCompletion: (() -> Unit)?)
    fun start()
    fun pause()
    fun getCurrentPosition(): Int?
    fun release()
}