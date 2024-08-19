package com.example.playlistmaker.audioplayer.domain.repository

import com.example.playlistmaker.audioplayer.domain.api.StatusObserver


interface AudioPlayerRepository {
    fun setSource(src: String)
    fun prepare()
    fun setStatusObserver(statusObserver: StatusObserver)
    fun start()
    fun pause()
    fun release()
}