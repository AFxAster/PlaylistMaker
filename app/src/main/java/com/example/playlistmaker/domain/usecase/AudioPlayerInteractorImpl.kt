package com.example.playlistmaker.domain.usecase

import com.example.playlistmaker.domain.api.AudioPlayerInteractor
import com.example.playlistmaker.domain.repository.AudioPlayerRepository


class AudioPlayerInteractorImpl(private val audioPlayerRepository: AudioPlayerRepository) :
    AudioPlayerInteractor {

    override fun initWithSource(src: String, onPrepared: (() -> Unit)?) {
        audioPlayerRepository.initWithSource(src)
        audioPlayerRepository.setOnPreparedListener(onPrepared)
    }

    override fun setOnCompletionListener(onCompletion: (() -> Unit)?) {
        audioPlayerRepository.setOnCompletionListener(onCompletion)
    }

    override fun start() {
        audioPlayerRepository.start()
    }

    override fun pause() {
        audioPlayerRepository.pause()
    }

    override fun getCurrentPosition(): Int? = audioPlayerRepository.getCurrentPosition()
    override fun release() {
        audioPlayerRepository.release()
    }

}