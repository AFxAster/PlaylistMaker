package com.example.playlistmaker.search.data.network

import com.example.playlistmaker.released.data.dto.GetArtistsRequest
import com.example.playlistmaker.released.data.dto.GetReleasesByArtistIdRequest
import com.example.playlistmaker.search.data.ITunesNetworkClient
import com.example.playlistmaker.search.data.dto.GetTrackByIdRequest
import com.example.playlistmaker.search.data.dto.GetTracksRequest
import com.example.playlistmaker.search.data.dto.Response
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ITunesRetrofitNetworkClient(private val api: ITunesApi) : ITunesNetworkClient {
    // TODO добавить обработку нет интернета, может уже в отдельной задаче проводить рефакторинг
    // TODO поменять на doRequest

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

    override suspend fun getReleasesByArtistId(requestParams: Any): Response {
        return if (requestParams !is GetReleasesByArtistIdRequest)
            Response().apply { responseCode = 400 }
        else
            withContext(Dispatchers.IO) {
                try {
                    api.getReleasesByArtistId(requestParams.artistId).apply { responseCode = 200 }
                } catch (ex: Exception) {
                    Response().apply { responseCode = 400 }
                }
            }
    }

    override suspend fun getArtists(requestParams: Any): Response {
        return if (requestParams !is GetArtistsRequest)
            Response().apply { responseCode = 400 }
        else
            withContext(Dispatchers.IO) {
                try {
                    api.getArtists(requestParams.query).apply { responseCode = 200 }
                } catch (ex: Exception) {
                    Response().apply { responseCode = 400 }
                }
            }
    }
}