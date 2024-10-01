package com.example.playlistmaker.search.data.network

import com.example.playlistmaker.search.data.dto.ITunesResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ITunesApi {
    @GET("/search?entity=song")
    suspend fun getTracks(@Query("term") queryInput: String): ITunesResponse

    @GET("/lookup?entity=song")
    suspend fun getTrackByID(@Query("id") id: String): ITunesResponse
}