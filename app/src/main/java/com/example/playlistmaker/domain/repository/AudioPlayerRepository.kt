package com.example.playlistmaker.domain.repository


interface AudioPlayerRepository {
    fun initWithSource(src: String)
    fun setOnPreparedListener(onPrepared: (() -> Unit)?)
    fun setOnCompletionListener(onCompletion: (() -> Unit)?)
    fun start()
    fun pause()
    fun getCurrentPosition(): Int?
    fun release()
}