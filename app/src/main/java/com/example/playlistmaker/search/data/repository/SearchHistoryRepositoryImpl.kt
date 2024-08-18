package com.example.playlistmaker.search.data.repository

import android.content.SharedPreferences
import com.example.playlistmaker.search.data.dto.HistoryTrackDTO
import com.example.playlistmaker.search.data.mapper.toHistoryTrackDTO
import com.example.playlistmaker.search.data.mapper.toTrack
import com.example.playlistmaker.search.domain.entity.Track
import com.example.playlistmaker.search.domain.repository.SearchHistoryRepository
import com.google.gson.Gson

class SearchHistoryRepositoryImpl(private val sharedPreferences: SharedPreferences) :
    SearchHistoryRepository {
    override fun getTracks(): List<Track> {
        val tracksDTO = Gson().fromJson(
            sharedPreferences.getString(
                SEARCH_HISTORY_KEY,
                null
            ),
            Array<HistoryTrackDTO>::class.java
        )?.toList() ?: emptyList()
        return tracksDTO.map { it.toTrack() }
    }

    override fun saveTracks(tracks: List<Track>) {
        sharedPreferences.edit().putString(
            SEARCH_HISTORY_KEY,
            Gson().toJson(tracks.map { it.toHistoryTrackDTO() })
        ).apply()
    }

    override fun clear() {
        sharedPreferences.edit().remove(SEARCH_HISTORY_KEY).apply()
    }

    private companion object {
        const val SEARCH_HISTORY_KEY = "SEARCH_HISTORY_KEY"
    }
}