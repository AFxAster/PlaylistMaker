package com.example.playlistmaker.search.domain.interactor

import com.example.playlistmaker.common.entity.Track
import com.example.playlistmaker.search.domain.api.SearchHistoryInteractor
import com.example.playlistmaker.search.domain.repository.SearchHistoryRepository

class SearchHistoryInteractorImpl(private val historyRepository: SearchHistoryRepository) :
    SearchHistoryInteractor {
    override fun getTracks(): List<Track> =
        historyRepository.getTracks()


    override fun saveTracks(tracks: List<Track>) {
        historyRepository.saveTracks(tracks)
    }

    override fun addTrack(track: Track) {
        var historyList: MutableList<Track> = getTracks().toMutableList()

        historyList.add(0, track)
        historyList = historyList.distinctBy { it.trackId }.take(10).toMutableList()
        saveTracks(historyList)
    }

    override fun clear() {
        historyRepository.clear()
    }
}