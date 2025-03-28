package com.example.playlistmaker.released.data

import com.example.playlistmaker.released.data.db.FollowedArtistEntity
import com.example.playlistmaker.released.data.dto.ArtistDTO
import com.example.playlistmaker.released.data.dto.ReleaseDTO
import com.example.playlistmaker.released.domain.entity.Artist
import com.example.playlistmaker.released.domain.entity.Release
import com.example.playlistmaker.released.domain.entity.ReleaseType

fun ReleaseDTO.toRelease(): Release {
    return Release(
        id = collectionId,
        artistName = artistName,
        artworkUrl100 = artworkUrl100,
        releaseDate = releaseDate,
        releaseName = collectionName.removeSuffix(" - Single"),
        type = collectionName.run {
            if (endsWith(" - Single"))
                ReleaseType.Single
            else
                ReleaseType.Album
        },
    )
}

fun Artist.toFollowedArtistEntity(): FollowedArtistEntity {
    return FollowedArtistEntity(
        id = id,
        name = name
    )
}

fun FollowedArtistEntity.toArtist(): Artist {
    return Artist(
        id = id,
        name = name
    )
}

fun ArtistDTO.toArtist(): Artist {
    return Artist(
        id = id,
        name = name
    )
}