package com.example.playlistmaker.search

import android.content.SharedPreferences
import com.example.playlistmaker.Track
import com.google.gson.Gson

class SearchHistory(private val sharedPreferences: SharedPreferences) {
    private val SEARCH_HISTORY_KEY = "SEARCH_HISTORY_KEY"

    fun getHistoryList(): List<Track> = Gson().fromJson(
        sharedPreferences.getString(
            SEARCH_HISTORY_KEY,
            null
        ),
        Array<Track>::class.java
    )?.toList() ?: emptyList()


    fun add(track: Track) {
        var historyList: MutableList<Track> = getHistoryList().toMutableList()

        historyList.add(0, track)
        historyList = historyList.distinctBy { it.trackId }.take(10).toMutableList()
        sharedPreferences.edit().putString(
            SEARCH_HISTORY_KEY,
            Gson().toJson(historyList)
        ).apply()
    }

    fun clear() {
        sharedPreferences.edit().remove(SEARCH_HISTORY_KEY).apply()
    }
}