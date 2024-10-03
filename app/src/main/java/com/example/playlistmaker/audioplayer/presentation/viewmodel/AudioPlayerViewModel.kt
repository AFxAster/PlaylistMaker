package com.example.playlistmaker.audioplayer.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.audioplayer.domain.api.AudioPlayerInteractor
import com.example.playlistmaker.audioplayer.domain.api.StatusObserver
import com.example.playlistmaker.audioplayer.presentation.mapper.toFormattedPosition
import com.example.playlistmaker.audioplayer.presentation.mapper.toPlayerTrackUI
import com.example.playlistmaker.audioplayer.presentation.state.AudioPlayerState
import com.example.playlistmaker.audioplayer.presentation.state.PlayingStatus
import com.example.playlistmaker.search.domain.api.TracksInteractor
import com.example.playlistmaker.search.domain.entity.Track
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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
        viewModelScope.launch {
            tracksInteractor.getTrackById(trackId).collect { track ->
                if (track == null) {
                    state.value = AudioPlayerState.Error
                } else {
                    preparePlayer(track)
                }
            }
        }
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
        var timerJob: Job? = null
        audioPlayerInteractor.setSource(track.previewUrl)
        audioPlayerInteractor.setStatusObserver(object : StatusObserver {
            override fun onPrepared() {
                state.value = AudioPlayerState.Content(track.toPlayerTrackUI())
            }

            override fun onPlay(position: Int) {
                timerJob = viewModelScope.launch {
                    do {
                        playingStatus.value = PlayingStatus.Playing(
                            audioPlayerInteractor.getCurrentPosition().toFormattedPosition()
                        )
                        delay(REFRESH_PLAY_PROGRESS_DELAY)
                    } while (playingStatus.value is PlayingStatus.Playing)
                }
            }

            override fun onPause(position: Int) {
                playingStatus.value = PlayingStatus.Paused(position.toFormattedPosition())
                timerJob?.cancel()
            }

            override fun onCompletion() {
                playingStatus.value = PlayingStatus.Completed
                timerJob?.cancel()
            }
        })
        audioPlayerInteractor.prepare()
    }

    override fun onCleared() {
        super.onCleared()
        audioPlayerInteractor.release()
    }

    private companion object {
        const val REFRESH_PLAY_PROGRESS_DELAY = 300L
    }
}