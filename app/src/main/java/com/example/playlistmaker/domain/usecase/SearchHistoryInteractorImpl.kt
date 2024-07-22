package com.example.playlistmaker.domain.usecase

import com.example.playlistmaker.domain.api.SearchHistoryInteractor
import com.example.playlistmaker.domain.entity.Track
import com.example.playlistmaker.domain.repository.SearchHistoryRepository

class SearchHistoryInteractorImpl(private val historyRepository: SearchHistoryRepository):
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