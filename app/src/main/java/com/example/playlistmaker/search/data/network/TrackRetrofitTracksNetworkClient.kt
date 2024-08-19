package com.example.playlistmaker.search.data.network

import com.example.playlistmaker.search.data.TracksNetworkClient
import com.example.playlistmaker.search.data.dto.GetTrackByIdRequest
import com.example.playlistmaker.search.data.dto.GetTracksRequest
import com.example.playlistmaker.search.data.dto.Response

class TrackRetrofitTracksNetworkClient(private val api: ITunesApi) : TracksNetworkClient {

    override fun getTracks(requestParams: Any): Response {
        return if (requestParams is GetTracksRequest) {
            try {
                val response = api.getTracks(requestParams.query).execute()
                val responseBody = response.body() ?: Response()

                responseBody.apply { responseCode = response.code() }
            } catch (ex: Exception) {
                Response().apply { responseCode = 400 }
            }
        } else {
            Response().apply { responseCode = 400 }
        }
    }

    override fun getTrackById(requestParams: Any): Response {
        return if (requestParams is GetTrackByIdRequest) {
            try {
                val response = api.getTrackByID(requestParams.id).execute()
                val responseBody = response.body() ?: Response()

                responseBody.apply { responseCode = response.code() }
            } catch (ex: Exception) {
                Response().apply { responseCode = 400 }
            }
        } else {
            Response().apply { responseCode = 400 }
        }
    }
}