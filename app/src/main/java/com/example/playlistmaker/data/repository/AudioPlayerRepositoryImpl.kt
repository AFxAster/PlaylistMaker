package com.example.playlistmaker.data.repository

import android.media.MediaPlayer
import com.example.playlistmaker.domain.repository.AudioPlayerRepository

class AudioPlayerRepositoryImpl(private val mediaPlayer: MediaPlayer) : AudioPlayerRepository {
    private var playerState = PLAYER_STATE_DEFAULT
    override fun initWithSource(src: String) {
        if (playerState != PLAYER_STATE_DEFAULT) return
        mediaPlayer.setDataSource(src)
        mediaPlayer.prepareAsync()
    }

    override fun setOnPreparedListener(onPrepared: (() -> Unit)?) {
        mediaPlayer.setOnPreparedListener {
            playerState = PLAYER_STATE_PREPARED
            onPrepared?.invoke()
        }
    }

    override fun setOnCompletionListener(onCompletion: (() -> Unit)?) {
        mediaPlayer.setOnCompletionListener {
            it.seekTo(0)
            onCompletion?.invoke()
        }
    }

    override fun start() {
        if (playerState == PLAYER_STATE_DEFAULT) return
        mediaPlayer.start()
    }

    override fun pause() {
        if (playerState == PLAYER_STATE_DEFAULT) return
        mediaPlayer.pause()
    }

    override fun getCurrentPosition(): Int? =
        if (playerState == PLAYER_STATE_DEFAULT) null else mediaPlayer.currentPosition

    override fun release() {
        mediaPlayer.release()
    }

    private companion object {
        const val PLAYER_STATE_DEFAULT = 0
        const val PLAYER_STATE_PREPARED = 1
    }
}