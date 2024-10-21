package com.example.playlistmaker.search.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.common.utils.debounce
import com.example.playlistmaker.search.domain.api.SearchHistoryInteractor
import com.example.playlistmaker.search.domain.api.TracksInteractor
import com.example.playlistmaker.search.presentation.mapper.toTrackUI
import com.example.playlistmaker.search.presentation.state.SearchState
import kotlinx.coroutines.launch

class SearchViewModel(
    private val tracksInteractor: TracksInteractor,
    private val searchHistoryInteractor: SearchHistoryInteractor
) : ViewModel() {

    private val state: MutableLiveData<SearchState> = MutableLiveData()
    var lastQuery: String = ""
        private set

    private val debounceRequestLambda = debounce<String>(
        delayMillis = REQUEST_DELAY,
        coroutineScope = viewModelScope,
        useLastCall = true,
        action = ::request
    )

    init {
        loadHistory()
    }

    fun getState(): LiveData<SearchState> = state

    fun debounceRequest(query: String) {
        state.value = SearchState.Loading
        lastQuery = query

        debounceRequestLambda(query)
        if (query.isBlank()) {
            loadHistory()
        }
    }

    fun refresh() {
        debounceRequest(lastQuery)
    }

    fun addTrackToHistory(trackId: String) {
        viewModelScope.launch {
            tracksInteractor.getTrackById(trackId).collect { track ->
                track?.let {
                    searchHistoryInteractor.addTrack(track)
                    if (state.value is SearchState.History)
                        loadHistory()
                }
            }
        }

    }

    fun clearHistory() {
        searchHistoryInteractor.clear()
        state.value = SearchState.History(emptyList())
    }

    private fun request(query: String) {
        if (query.isBlank()) return
        viewModelScope.launch {
            tracksInteractor.getTracks(query).collect { tracks ->
                if (tracks == null) {
                    state.value = SearchState.Error
                    return@collect
                }
                if (tracks.isNotEmpty()) {
                    state.value = SearchState.Content(tracks.map { it.toTrackUI() })
                } else {
                    state.value = SearchState.Empty
                }
            }
        }
    }

    private fun loadHistory() {
        state.value = SearchState.Loading
        state.value =
            SearchState.History(searchHistoryInteractor.getTracks().map { it.toTrackUI() })
    }

    companion object {
        private const val REQUEST_DELAY = 2000L
    }
}