package ru.ikrom.youtube_data.extensions

import ru.ikrom.database.ArtistEntity
import ru.ikrom.database.TrackEntity
import ru.ikrom.youtube_data.model.ArtistModel
import ru.ikrom.youtube_data.model.TrackModel
import ru.ikrom.youtube_data.model.getNames

fun TrackModel.toEntity(): TrackEntity {
    return TrackEntity(
        id = videoId,
        title = title,
        artist = album.artists.getNames(),
        thumbnail = album.thumbnail,
        albumId = album.id,
        artistId = album.artists?.firstOrNull()?.id ?: "",
        isDownloaded = false
    )
}

fun ArtistModel.toEntity(): ArtistEntity? {
    return this.id?.let {
        ArtistEntity(
        id = it,
        name = this.name,
        thumbnail = this.thumbnail
    )
    }
}