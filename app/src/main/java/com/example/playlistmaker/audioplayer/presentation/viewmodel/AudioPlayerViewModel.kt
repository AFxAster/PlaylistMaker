package com.example.playlistmaker.audioplayer.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.audioplayer.domain.api.AudioPlayerInteractor
import com.example.playlistmaker.audioplayer.domain.api.StatusObserver
import com.example.playlistmaker.audioplayer.presentation.mapper.toFormattedPosition
import com.example.playlistmaker.audioplayer.presentation.state.AudioPlayerState
import com.example.playlistmaker.audioplayer.presentation.state.PlayingStatus
import com.example.playlistmaker.common.entity.Track
import com.example.playlistmaker.favourite.domain.api.FavouriteTracksInteractor
import com.example.playlistmaker.search.domain.api.TracksInteractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class AudioPlayerViewModel(
    private val trackId: String,
    private val tracksInteractor: TracksInteractor,
    private val audioPlayerInteractor: AudioPlayerInteractor,
    private val favouriteTracksInteractor: FavouriteTracksInteractor
) : ViewModel() {

    private val state: MutableLiveData<AudioPlayerState> = MutableLiveData()
    private val playingStatus: MutableLiveData<PlayingStatus> = MutableLiveData()
    private val isFavourite: MutableLiveData<Boolean> = MutableLiveData()

    init {
        loadData()
    }

    fun getState(): LiveData<AudioPlayerState> = state

    fun getPlayingStatus(): LiveData<PlayingStatus> = playingStatus

    fun getIsFavourite(): LiveData<Boolean> = isFavourite

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
                state.value = AudioPlayerState.Content(track)
                isFavourite.value = track.isFavourite
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

    fun changeFavourite() {
        viewModelScope.launch(Dispatchers.IO) {
            val track = (state.value as AudioPlayerState.Content).data
            if (isFavourite.value == false) {
                favouriteTracksInteractor.addFavouriteTrack(track)
            } else {
                favouriteTracksInteractor.deleteFavouriteTrack(track)
            }
            isFavourite.postValue(!isFavourite.value!!)
        }

    }

    override fun onCleared() {
        super.onCleared()
        audioPlayerInteractor.release()
    }

    private companion object {
        const val REFRESH_PLAY_PROGRESS_DELAY = 300L
    }
}