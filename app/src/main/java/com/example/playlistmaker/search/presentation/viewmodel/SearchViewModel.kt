package com.example.playlistmaker.search.presentation.viewmodel

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.search.domain.api.SearchHistoryInteractor
import com.example.playlistmaker.search.domain.api.TracksInteractor
import com.example.playlistmaker.search.domain.entity.Track
import com.example.playlistmaker.search.presentation.mapper.toTrackUI
import com.example.playlistmaker.search.presentation.state.SearchState

class SearchViewModel(
    private val tracksInteractor: TracksInteractor,
    private val searchHistoryInteractor: SearchHistoryInteractor
) : ViewModel() {

    private val state: MutableLiveData<SearchState> = MutableLiveData()
    private val handler = Handler(Looper.getMainLooper())
    var lastQuery: String = ""
        private set

    private val tracksConsumer: TracksInteractor.TracksConsumer =
        object : TracksInteractor.TracksConsumer {
            override fun onTracksSuccess(tracks: List<Track>) {
                if (tracks.isNotEmpty()) {
                    state.postValue(SearchState.Content(tracks.map { it.toTrackUI() }))
                } else {
                    state.postValue(SearchState.Empty)
                }
            }

            override fun onError() {
                state.postValue(SearchState.Error)
            }
        }

    fun getState(): LiveData<SearchState> = state

    init {
        loadHistory()
    }

    fun debounceRequest(query: String) {
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
        if (query.isBlank()) {
            loadHistory()
            return
        }
        state.value = SearchState.Loading
        lastQuery = query

        val requestRunnable = Runnable {
            tracksInteractor.getTracks(query, tracksConsumer)
        }
        handler.postDelayed(requestRunnable, SEARCH_REQUEST_TOKEN, REQUEST_DELAY)
    }

    fun refresh() {
        debounceRequest(lastQuery)
    }

    fun addTrackToHistory(trackId: String) {
        tracksInteractor.getTrackById(trackId, object : TracksInteractor.TrackByIdConsumer {
            override fun onTrackByIdSuccess(track: Track) {
                searchHistoryInteractor.addTrack(track)
                if (state.value is SearchState.History)
                    handler.post { loadHistory() }
            }

            override fun onError() {}
        })
    }

    fun clearHistory() {
        searchHistoryInteractor.clear()
        state.value = SearchState.History(emptyList())
    }

    private fun loadHistory() {
        state.value = SearchState.Loading
        state.value =
            SearchState.History(searchHistoryInteractor.getTracks().map { it.toTrackUI() })
    }

    override fun onCleared() {
        super.onCleared()
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
    }

    companion object {
        private const val REQUEST_DELAY = 2000L
        private val SEARCH_REQUEST_TOKEN = Any()
    }
}