package com.example.playlistmaker.released.data.dto

import com.google.gson.annotations.SerializedName

data class ArtistDTO(
    @SerializedName("artistId")
    val id: String,
    @SerializedName("artistName")
    val name: String
)
