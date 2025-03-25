package com.example.playlistmaker.released.data.repository

import com.example.playlistmaker.common.data.db.AppDatabase
import com.example.playlistmaker.released.data.toArtist
import com.example.playlistmaker.released.data.toFollowedArtistEntity
import com.example.playlistmaker.released.domain.entity.Artist
import com.example.playlistmaker.released.domain.repository.FollowedArtistsRepository

class FollowedArtistsRepositoryImpl(
    private val database: AppDatabase
) : FollowedArtistsRepository {
    override fun addFollowedArtist(artist: Artist) {
        database.getArtistDao().insertFollowedArtist(artist.toFollowedArtistEntity())
    }

    override fun deleteFollowedArtist(artist: Artist) {
        database.getArtistDao().deleteFollowedArtist(artist.toFollowedArtistEntity())
    }

    override fun getFollowedArtists(): List<Artist> {
        return database.getArtistDao().getFollowedArtists().map { it.toArtist() }
    }
}