package com.example.playlistmaker.released.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.released.domain.api.ReleasedInteractor
import kotlinx.coroutines.launch
import java.util.Date

class ReleasedViewModel(
    private val releasedInteractor: ReleasedInteractor
) : ViewModel() {

    private val state: MutableLiveData<ReleasedState> = MutableLiveData()
    fun getState(): LiveData<ReleasedState> = state // TODO отрефакторить на поля

    init {
        loadData()
    }

    private fun loadData() {
        state.value = ReleasedState.Loading
        viewModelScope.launch {
            releasedInteractor.getReleasedFrom(Date()).collect {
                it?.let {
                    state.value = ReleasedState.Content(it)
                }
            }
        }
    }
}