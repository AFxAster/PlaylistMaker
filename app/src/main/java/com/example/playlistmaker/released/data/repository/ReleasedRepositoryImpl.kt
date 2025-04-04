package com.example.playlistmaker.released.data.repository

import com.example.playlistmaker.released.data.dto.GetReleasesByArtistIdRequest
import com.example.playlistmaker.released.data.dto.ReleasesByArtistResponse
import com.example.playlistmaker.released.data.toRelease
import com.example.playlistmaker.released.domain.entity.Artist
import com.example.playlistmaker.released.domain.entity.Release
import com.example.playlistmaker.released.domain.repository.ReleasedRepository
import com.example.playlistmaker.search.data.ITunesNetworkClient
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.net.HttpURLConnection
import java.util.Date


class ReleasedRepositoryImpl(
    private val iTunesNetworkClient: ITunesNetworkClient
) : ReleasedRepository {
    override fun getReleasesFrom(artist: Artist, date: Date): Flow<List<Release>?> = flow {
        val response =
            iTunesNetworkClient.getReleasesByArtistId(GetReleasesByArtistIdRequest(artist.id))
        if (response.responseCode == HttpURLConnection.HTTP_OK && response is ReleasesByArtistResponse) {
            val results = response.results.mapNotNull {
                if (it.collectionName == null || it.releaseDate.before(date))
                    null
                else
                    it.toRelease().apply {
                        tier = artist.tier
                    }
            }

            emit(results)
        } else {
            emit(null)
        }

    }
}
