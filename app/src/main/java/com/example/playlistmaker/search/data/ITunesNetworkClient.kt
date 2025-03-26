package com.example.playlistmaker.search.data

import com.example.playlistmaker.search.data.dto.Response

interface ITunesNetworkClient {

    suspend fun getTracks(requestParams: Any): Response

    suspend fun getTrackById(requestParams: Any): Response

    suspend fun getReleasesByArtistId(requestParams: Any): Response

    suspend fun getArtists(requestParams: Any): Response
}