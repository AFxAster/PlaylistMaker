package com.example.playlistmaker.data.network

import com.example.playlistmaker.data.TracksNetworkClient
import com.example.playlistmaker.data.dto.GetTrackByIdRequest
import com.example.playlistmaker.data.dto.GetTracksRequest
import com.example.playlistmaker.data.dto.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class TrackRetrofitTracksNetworkClient : TracksNetworkClient {
    private val api = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ITunesApi::class.java)

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

    private companion object {
        const val BASE_URL = "https://itunes.apple.com"
    }
}