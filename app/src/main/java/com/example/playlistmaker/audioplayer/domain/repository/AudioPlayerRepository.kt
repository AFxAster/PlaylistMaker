package com.example.playlistmaker.audioplayer.domain.repository

import com.example.playlistmaker.audioplayer.domain.api.AudioPlayerInteractor


interface AudioPlayerRepository {
    fun setSource(src: String)
    fun prepare()
    fun setStatusObserver(statusObserver: AudioPlayerInteractor.StatusObserver)
    fun start()
    fun pause()
    fun release()
}