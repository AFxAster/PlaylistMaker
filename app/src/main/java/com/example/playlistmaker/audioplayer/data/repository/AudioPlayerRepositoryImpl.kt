package com.example.playlistmaker.audioplayer.data.repository

import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import com.example.playlistmaker.audioplayer.domain.api.StatusObserver
import com.example.playlistmaker.audioplayer.domain.repository.AudioPlayerRepository

class AudioPlayerRepositoryImpl(private val mediaPlayer: MediaPlayer) : AudioPlayerRepository {
    private var statusObserver: StatusObserver? = null
    private val handler = Handler(Looper.getMainLooper())
    private val playProgressRefreshRunnable = object : Runnable {
        override fun run() {
            statusObserver?.onProgress(mediaPlayer.currentPosition)
            handler.postDelayed(this, REFRESH_PLAY_PROGRESS_DELAY)
        }
    }

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
            handler.removeCallbacks(playProgressRefreshRunnable)
        }
    }

    override fun start() {
        mediaPlayer.start()
        statusObserver?.onPlay(mediaPlayer.currentPosition)
        handler.postDelayed(playProgressRefreshRunnable, REFRESH_PLAY_PROGRESS_DELAY)
    }

    override fun pause() {
        if (!mediaPlayer.isPlaying) return
        mediaPlayer.pause()
        statusObserver?.onPause(mediaPlayer.currentPosition)
        handler.removeCallbacks(playProgressRefreshRunnable)
    }

    override fun release() {
        mediaPlayer.release()
        handler.removeCallbacks(playProgressRefreshRunnable)
    }

    private companion object {
        const val REFRESH_PLAY_PROGRESS_DELAY = 300L
    }
}