package com.example.playlistmaker.audioplayer.data.repository

import android.media.MediaPlayer
import com.example.playlistmaker.audioplayer.domain.api.StatusObserver
import com.example.playlistmaker.audioplayer.domain.repository.AudioPlayerRepository

class AudioPlayerRepositoryImpl(private val mediaPlayer: MediaPlayer) : AudioPlayerRepository {
    private var statusObserver: StatusObserver? = null

    override fun setSource(src: String) {
        mediaPlayer.setDataSource(src)
    }

    override fun prepare() {
        mediaPlayer.prepareAsync()
    }

    override fun setStatusObserver(statusObserver: StatusObserver) {
        this.statusObserver = statusObserver

        mediaPlayer.setOnPreparedListener {
            statusObserver.onPrepared()
        }

        mediaPlayer.setOnCompletionListener {
            statusObserver.onCompletion()
            mediaPlayer.seekTo(0)
        }
    }

    override fun start() {
        mediaPlayer.start()
        statusObserver?.onPlay(mediaPlayer.currentPosition)
    }

    override fun pause() {
        if (!mediaPlayer.isPlaying) return
        mediaPlayer.pause()
        statusObserver?.onPause(mediaPlayer.currentPosition)
    }

    override fun getCurrentPosition(): Int = mediaPlayer.currentPosition

    override fun release() {
        mediaPlayer.release()
    }
}