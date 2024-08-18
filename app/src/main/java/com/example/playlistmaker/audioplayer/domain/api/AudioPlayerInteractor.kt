package com.example.playlistmaker.audioplayer.domain.api

interface AudioPlayerInteractor {
    fun setSource(src: String)
    fun setStatusObserver(statusObserver: StatusObserver)
    fun prepare()
    fun start()
    fun pause()
    fun release()

    interface StatusObserver {
        fun onPrepared()
        fun onPlay(position: Int)
        fun onProgress(position: Int)
        fun onPause(position: Int)
        fun onCompletion()
    }
}