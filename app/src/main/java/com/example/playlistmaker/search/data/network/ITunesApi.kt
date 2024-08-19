package com.example.playlistmaker.search.data.network

import com.example.playlistmaker.search.data.dto.ITunesResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ITunesApi {
    @GET("/search?entity=song")
    fun getTracks(@Query("term") queryInput: String): Call<ITunesResponse>

    @GET("/lookup?entity=song")
    fun getTrackByID(@Query("id") id: String): Call<ITunesResponse>
}