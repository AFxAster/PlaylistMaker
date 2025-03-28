package com.example.playlistmaker.released.data.dto

import com.example.playlistmaker.search.data.dto.Response

data class ReleasesByArtistResponse(
    val resultCount: Int,
    val results: List<ReleaseDTO>
) : Response()