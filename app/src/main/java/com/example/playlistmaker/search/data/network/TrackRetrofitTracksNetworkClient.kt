package com.example.playlistmaker.search.data.network

import com.example.playlistmaker.search.data.TracksNetworkClient
import com.example.playlistmaker.search.data.dto.GetTrackByIdRequest
import com.example.playlistmaker.search.data.dto.GetTracksRequest
import com.example.playlistmaker.search.data.dto.Response
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TrackRetrofitTracksNetworkClient(private val api: ITunesApi) : TracksNetworkClient {

    override suspend fun getTracks(requestParams: Any): Response {
        return if (requestParams !is GetTracksRequest)
            Response().apply { responseCode = 400 }
        else
            withContext(Dispatchers.IO) {
                try {
                    api.getTracks(requestParams.query).apply { responseCode = 200 }
                } catch (ex: Exception) {
                    Response().apply { responseCode = 400 }
                }
            }
    }

    override suspend fun getTrackById(requestParams: Any): Response {
        return if (requestParams !is GetTrackByIdRequest)
            Response().apply { responseCode = 400 }
        else
            withContext(Dispatchers.IO) {
                try {
                    api.getTrackByID(requestParams.id).apply { responseCode = 200 }
                } catch (ex: Exception) {
                    Response().apply { responseCode = 400 }
                }
            }
    }
}