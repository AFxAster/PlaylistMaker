package com.example.playlistmaker.released.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.released.domain.api.ReleasedInteractor
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.ZoneId
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
            val lastFriday = getLastFridayFrom(Date())
            releasedInteractor.getReleasedFrom(lastFriday).collect {
                it?.let {
                    state.value = ReleasedState.Content(it)
                }
            }
        }
    }

    private fun getLastFridayFrom(date: Date): Date {
        val localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
        val dayOfWeek = localDate.dayOfWeek

        val daysToSubtract = if (dayOfWeek.value < DayOfWeek.FRIDAY.value) {
            dayOfWeek.value + 7 - DayOfWeek.FRIDAY.value
        } else {
            dayOfWeek.value - DayOfWeek.FRIDAY.value
        }

        val lastFridayLocalDate = localDate.minusDays(daysToSubtract.toLong())
        return Date.from(lastFridayLocalDate.atStartOfDay(ZoneId.systemDefault()).toInstant())
    }
}