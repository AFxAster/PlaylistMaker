package com.example.playlistmaker.data

import com.example.playlistmaker.data.dto.Response

interface TracksNetworkClient {
    fun getTracks(requestParams: Any): Response
    fun getTrackById(requestParams: Any): Response
}