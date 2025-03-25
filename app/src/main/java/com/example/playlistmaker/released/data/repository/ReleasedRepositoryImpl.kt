package com.example.playlistmaker.released.data.repository

import com.example.playlistmaker.released.data.toRelease
import com.example.playlistmaker.released.domain.entity.Release
import com.example.playlistmaker.released.domain.repository.ReleasedRepository
import com.example.playlistmaker.search.data.TracksNetworkClient
import com.example.playlistmaker.search.data.dto.GetReleasesByArtistIdRequest
import com.example.playlistmaker.search.data.dto.ReleasesByArtistResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.net.HttpURLConnection
import java.util.Date


class ReleasedRepositoryImpl(
    private val tracksNetworkClient: TracksNetworkClient
) : ReleasedRepository {
    override fun getReleasedFrom(date: Date): Flow<List<Release>?> = flow {
        val response =
            tracksNetworkClient.getReleasesByArtistId(GetReleasesByArtistIdRequest("1564157271"))
        if (response.responseCode == HttpURLConnection.HTTP_OK && response is ReleasesByArtistResponse) {
            val results = response.results.mapNotNull {
                if (it.collectionName == null || it.releaseDate.before(date))
                    null
                else
                    it.toRelease()
            }

            emit(results)
        } else {
            emit(null)
        }

    }
}
