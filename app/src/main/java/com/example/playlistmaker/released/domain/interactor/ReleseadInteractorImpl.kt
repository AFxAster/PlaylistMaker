package com.example.playlistmaker.released.domain.interactor

import com.example.playlistmaker.released.domain.api.ReleasedInteractor
import com.example.playlistmaker.released.domain.entity.Release
import com.example.playlistmaker.released.domain.repository.FollowedArtistsRepository
import com.example.playlistmaker.released.domain.repository.ReleasedRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.util.Date

class ReleseadInteractorImpl(
    private val releasedRepository: ReleasedRepository,
    private val followedArtistsRepository: FollowedArtistsRepository
) : ReleasedInteractor {
    override fun getReleasesFrom(date: Date): Flow<List<Release>?> = flow {
        val releases: MutableList<Release> = mutableListOf()
        followedArtistsRepository.getFollowedArtists().collect followed@{ followedArtists ->
            if (followedArtists == null) {
                emit(null)
                return@followed
            }

            followedArtists.forEach { artist ->
                releasedRepository.getReleasesFrom(artist.id, date).collect releases@{
                    it ?: return@releases
                    releases.addAll(it)
                }
            }
        }
        emit(releases)
    }.flowOn(Dispatchers.IO)
}