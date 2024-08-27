package com.example.playlistmaker.library.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.library.presentation.state.FavouriteState

class FavouriteViewModel : ViewModel() {

    private val state: MutableLiveData<FavouriteState> = MutableLiveData()
    fun getState(): LiveData<FavouriteState> = state

    init {
        state.value = FavouriteState.Empty
    }
}