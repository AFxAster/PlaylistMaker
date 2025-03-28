package com.example.playlistmaker.search.data.network

import com.example.playlistmaker.released.data.dto.ArtistsResponse
import com.example.playlistmaker.released.data.dto.ReleasesByArtistResponse
import com.example.playlistmaker.search.data.dto.TracksResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ITunesApi {
    @GET("/search?entity=song")
    suspend fun getTracks(@Query("term") queryInput: String): TracksResponse

    @GET("/lookup?entity=song")
    suspend fun getTrackByID(@Query("id") id: String): TracksResponse

    @GET("/lookup?entity=album")
    suspend fun getReleasesByArtistId(@Query("id") id: String): ReleasesByArtistResponse // todo обернуть в Response<>

    @GET("/search?entity=musicArtist")
    suspend fun getArtists(@Query("term") queryInput: String): ArtistsResponse
}