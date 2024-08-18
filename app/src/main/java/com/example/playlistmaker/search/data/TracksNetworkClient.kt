package com.example.playlistmaker.search.data

import com.example.playlistmaker.search.data.dto.Response

interface TracksNetworkClient {
    fun getTracks(requestParams: Any): Response
    fun getTrackById(requestParams: Any): Response
}