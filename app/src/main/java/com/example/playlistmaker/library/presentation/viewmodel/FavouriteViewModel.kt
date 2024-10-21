package com.example.playlistmaker.library.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.library.domain.api.FavouriteTracksInteractor
import com.example.playlistmaker.library.presentation.state.FavouriteState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavouriteViewModel(
    private val favouriteTracksInteractor: FavouriteTracksInteractor
) : ViewModel() {

    private val state: MutableLiveData<FavouriteState> = MutableLiveData()
    fun getState(): LiveData<FavouriteState> = state

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch(Dispatchers.IO) {
            favouriteTracksInteractor.getFlowableFavouriteTracks().collect { tracks ->
                if (tracks.isNotEmpty()) {
                    state.postValue(FavouriteState.Content(tracks.reversed()))
                } else {
                    state.postValue(FavouriteState.Empty)
                }
            }
        }
    }
}