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
        artworkUrl512 = artworkUrl100.replaceAfterLast('/', "512x512bb.jpg"),
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
        name = name,
        tier = tier
    )
}

fun FollowedArtistEntity.toArtist(): Artist {
    return Artist(
        id = id,
        name = name,
        tier = tier
    )
}

fun ArtistDTO.toArtist(): Artist {
    return Artist(
        id = id,
        name = name
    )
}