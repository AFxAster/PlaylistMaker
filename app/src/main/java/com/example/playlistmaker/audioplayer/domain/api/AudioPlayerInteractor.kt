package com.example.playlistmaker.audioplayer.domain.api

interface AudioPlayerInteractor {
    fun setSource(src: String)
    fun setStatusObserver(statusObserver: StatusObserver)
    fun prepare()
    fun start()
    fun pause()
    fun getCurrentPosition(): Int
    fun release()
}