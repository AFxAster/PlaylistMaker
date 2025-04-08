package com.example.playlistmaker.released.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.common.utils.debounceWithLastCall
import com.example.playlistmaker.released.domain.api.ArtistsInteractor
import com.example.playlistmaker.released.domain.api.ReleasedInteractor
import com.example.playlistmaker.released.domain.entity.Artist
import com.example.playlistmaker.released.presentation.state.ArtistState
import com.example.playlistmaker.released.presentation.state.ReleasedState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.ZoneId
import java.util.Calendar

class ReleasedViewModel(
    private val releasedInteractor: ReleasedInteractor,
    private val artistsInteractor: ArtistsInteractor
) : ViewModel() {

    private val state: MutableLiveData<ReleasedState> = MutableLiveData()
    fun getState(): LiveData<ReleasedState> = state // TODO отрефакторить на поля

    private val artistState: MutableLiveData<ArtistState> = MutableLiveData()
    fun getArtistState(): LiveData<ArtistState> = artistState

    var selectedArtist: Artist? = null

    var startDate: Calendar? = null
    var endDate: Calendar? = null

    private val debounceArtistRequestLambda = debounceWithLastCall<String>(
        delayMillis = ARTIST_REQUEST_DELAY,
        coroutineScope = viewModelScope,
        action = ::artistRequest
    )

    private var releasesJob: Job? = null

    init {
        startDate = getLastFridayFrom(Calendar.getInstance())
        endDate = Calendar.getInstance().apply {
            set(Calendar.HOUR, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        loadData()
    }

    fun loadData() {
        releasesJob?.cancel()
        state.value = ReleasedState.Loading
        releasesJob = viewModelScope.launch {
            releasedInteractor.getReleases(startDate, endDate).collect {
                it?.let {
                    state.value = ReleasedState.Content(it)
                }
            }
        }
    }

    fun debounceRequest(query: String) {
        artistState.value = ArtistState.Loading
        debounceArtistRequestLambda(query)
    }

    private fun artistRequest(query: String) {
        viewModelScope.launch {
            artistsInteractor.getArtists(query).collect {
                it ?: return@collect
                artistState.value = ArtistState.Content(it)
            }
        }
    }

    fun clearArtists() {
        artistState.value = ArtistState.Content(emptyList())
    }

    fun followSelectedArtist(tier: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            selectedArtist?.let {
                artistsInteractor.followArtist(it.copy(tier = tier))
            }
        }
    }

    private companion object {
        const val ARTIST_REQUEST_DELAY = 1000L
    }
}

private fun getLastFridayFrom(date: Calendar): Calendar {
    val localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
    val dayOfWeek = localDate.dayOfWeek

    val daysToSubtract = if (dayOfWeek.value < DayOfWeek.FRIDAY.value) {
        dayOfWeek.value + 7 - DayOfWeek.FRIDAY.value
    } else {
        dayOfWeek.value - DayOfWeek.FRIDAY.value
    }

    val lastFridayLocalDate = localDate.minusDays(daysToSubtract.toLong())
    val calendar = Calendar.getInstance()
    calendar.set(
        lastFridayLocalDate.year,
        lastFridayLocalDate.monthValue - 1,
        lastFridayLocalDate.dayOfMonth,
        0,
        0,
        0
    )
    calendar.set(Calendar.MILLISECOND, 0)
    return calendar
}