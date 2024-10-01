package com.example.playlistmaker.search.data

import com.example.playlistmaker.search.data.dto.Response

interface TracksNetworkClient {
    suspend fun getTracks(requestParams: Any): Response
    suspend fun getTrackById(requestParams: Any): Response
}