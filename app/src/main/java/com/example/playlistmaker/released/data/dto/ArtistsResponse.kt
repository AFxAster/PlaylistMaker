package com.example.playlistmaker.released.data.dto

import com.example.playlistmaker.search.data.dto.Response

data class ArtistsResponse(
    val resultCount: Int,
    val results: List<ArtistDTO>
) : Response()