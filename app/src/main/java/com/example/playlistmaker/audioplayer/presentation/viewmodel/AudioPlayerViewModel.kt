package com.example.playlistmaker.audioplayer.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.audioplayer.domain.api.AudioPlayerInteractor
import com.example.playlistmaker.audioplayer.domain.api.StatusObserver
import com.example.playlistmaker.audioplayer.presentation.mapper.toFormattedPosition
import com.example.playlistmaker.audioplayer.presentation.mapper.toPlayerTrackUI
import com.example.playlistmaker.audioplayer.presentation.state.AudioPlayerState
import com.example.playlistmaker.audioplayer.presentation.state.PlayingStatus
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.search.domain.api.TracksInteractor
import com.example.playlistmaker.search.domain.entity.Track

class AudioPlayerViewModel(
    private val trackId: String,
    private val tracksInteractor: TracksInteractor,
    private val audioPlayerInteractor: AudioPlayerInteractor
) : ViewModel() {

    private val state: MutableLiveData<AudioPlayerState> = MutableLiveData()
    private val playingStatus: MutableLiveData<PlayingStatus> = MutableLiveData()

    init {
        loadData()
    }

    fun getState(): LiveData<AudioPlayerState> = state
    fun getPlayingStatus(): LiveData<PlayingStatus> = playingStatus
    private fun loadData() {
        state.value = AudioPlayerState.Loading
        tracksInteractor.getTrackById(trackId, object : TracksInteractor.TrackByIdConsumer {
            override fun onTrackByIdSuccess(track: Track) {
                preparePlayer(track)
            }

            override fun onError() {
                state.postValue(AudioPlayerState.Error)
            }
        })
    }

    fun refresh() {
        loadData()
    }

    fun play() {
        audioPlayerInteractor.start()
    }

    fun pause() {
        audioPlayerInteractor.pause()
    }

    private fun preparePlayer(track: Track) {
        audioPlayerInteractor.setSource(track.previewUrl)
        audioPlayerInteractor.setStatusObserver(object : StatusObserver {
            override fun onPrepared() {
                state.value = AudioPlayerState.Content(track.toPlayerTrackUI())
            }

            override fun onPlay(position: Int) {
                playingStatus.value = PlayingStatus.Playing(position.toFormattedPosition())
            }

            override fun onProgress(position: Int) {
                playingStatus.value = PlayingStatus.Playing(position.toFormattedPosition())
            }

            override fun onPause(position: Int) {
                playingStatus.value = PlayingStatus.Paused(position.toFormattedPosition())
            }

            override fun onCompletion() {
                playingStatus.value = PlayingStatus.Completed
            }
        })
        audioPlayerInteractor.prepare()
    }

    override fun onCleared() {
        super.onCleared()
        audioPlayerInteractor.release()
    }

    companion object {
        fun getViewModelFactory(trackId: String): ViewModelProvider.Factory {
            return viewModelFactory {
                initializer {
                    AudioPlayerViewModel(
                        trackId,
                        Creator.provideTracksInteractor(),
                        Creator.provideAudioPlayerInteractor()
                    )
                }
            }
        }
    }
}