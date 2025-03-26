package com.example.playlistmaker.released.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.common.utils.debounceWithLastCall
import com.example.playlistmaker.released.domain.api.ReleasedInteractor
import com.example.playlistmaker.released.domain.entity.Artist
import com.example.playlistmaker.released.presentation.state.ArtistState
import com.example.playlistmaker.released.presentation.state.ReleasedState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.ZoneId
import java.util.Date

class ReleasedViewModel(
    private val releasedInteractor: ReleasedInteractor
) : ViewModel() {

    private val state: MutableLiveData<ReleasedState> = MutableLiveData()
    fun getState(): LiveData<ReleasedState> = state // TODO отрефакторить на поля

    private val artistState: MutableLiveData<ArtistState> = MutableLiveData()
    fun getArtistState(): LiveData<ArtistState> = artistState

    private val debounceArtistRequestLambda = debounceWithLastCall<String>(
        delayMillis = ARTIST_REQUEST_DELAY,
        coroutineScope = viewModelScope,
        action = ::artistRequest
    )

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

    fun debounceRequest(query: String) {
        artistState.value = ArtistState.Loading

        debounceArtistRequestLambda(query)
        if (query.isBlank()) {

        }
    }

    private fun artistRequest(query: String) {
        viewModelScope.launch {

            delay(3000)
            artistState.value = ArtistState.Content(
                listOf(
                    Artist(
                        id = "1",
                        name = "nttrl"
                    ),
                    Artist(
                        id = "2",
                        name = "МЫ"
                    ),
                )
            )
        }
    }

    private companion object {
        const val ARTIST_REQUEST_DELAY = 500L
    }
}