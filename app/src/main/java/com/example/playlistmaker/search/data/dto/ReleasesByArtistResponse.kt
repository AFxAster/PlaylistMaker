package com.example.playlistmaker.search.data.dto

data class ReleasesByArtistResponse(
    val resultCount: Int,
    val results: List<ReleaseDTO>
) : Response()