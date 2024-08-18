package com.example.playlistmaker.audioplayer.domain.interactor

import com.example.playlistmaker.audioplayer.domain.api.AudioPlayerInteractor
import com.example.playlistmaker.audioplayer.domain.repository.AudioPlayerRepository


class AudioPlayerInteractorImpl(private val audioPlayerRepository: AudioPlayerRepository) :
    AudioPlayerInteractor {

    override fun setSource(src: String) {
        audioPlayerRepository.setSource(src)
    }

    override fun setStatusObserver(statusObserver: AudioPlayerInteractor.StatusObserver) {
        audioPlayerRepository.setStatusObserver(statusObserver)
    }

    override fun prepare() {
        audioPlayerRepository.prepare()
    }

    override fun start() {
        audioPlayerRepository.start()
    }

    override fun pause() {
        audioPlayerRepository.pause()
    }

    override fun release() {
        audioPlayerRepository.release()
    }

}