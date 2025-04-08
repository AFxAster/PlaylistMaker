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
import java.util.Calendar


class ReleasedRepositoryImpl(
    private val iTunesNetworkClient: ITunesNetworkClient
) : ReleasedRepository {
    override fun getReleases(artist: Artist, from: Calendar?, to: Calendar?): Flow<List<Release>?> =
        flow {
            val response =
                iTunesNetworkClient.getReleasesByArtistId(GetReleasesByArtistIdRequest(artist.id))
            if (response.responseCode == HttpURLConnection.HTTP_OK && response is ReleasesByArtistResponse) {
                val results = response.results.mapNotNull {
                    if (it.collectionName == null) return@mapNotNull null

                    val releaseDate: Calendar = Calendar.getInstance()
                    releaseDate.time = it.releaseDate
                    releaseDate.set(Calendar.HOUR, 0)
                    releaseDate.set(Calendar.MINUTE, 0)
                    releaseDate.set(Calendar.SECOND, 0)
                    releaseDate.set(Calendar.MILLISECOND, 0)

                    if (from != null && releaseDate < from)
                        null
                    else if (to != null && releaseDate > to)
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
