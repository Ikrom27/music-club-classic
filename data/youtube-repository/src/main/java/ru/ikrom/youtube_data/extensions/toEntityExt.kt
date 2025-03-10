package ru.ikrom.youtube_data.extensions

import ru.ikrom.database.AlbumEntity
import ru.ikrom.database.ArtistEntity
import ru.ikrom.database.PlaylistEntity
import ru.ikrom.database.TrackEntity
import ru.ikrom.youtube_data.model.AlbumModel
import ru.ikrom.youtube_data.model.ArtistModel
import ru.ikrom.youtube_data.model.PlaylistModel
import ru.ikrom.youtube_data.model.TrackModel
import ru.ikrom.youtube_data.model.getNames

internal fun TrackModel.toEntity(): TrackEntity {
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

internal fun ArtistModel.toEntity(): ArtistEntity? {
    return this.id?.let {
        ArtistEntity(
        id = it,
        name = this.name,
        thumbnail = this.thumbnail
    )
    }
}

internal fun AlbumModel.toEntity(): AlbumEntity {
    return AlbumEntity(
        id = this.id,
        title = this.title,
        artistNames = this.artists.getNames(),
        artistId = this.artists?.first()?.id ?: "",
        thumbnail = this.thumbnail,
        year = this.year
    )
}

internal fun PlaylistModel.toEntity()
    = PlaylistEntity(
        id = id.toIntOrNull() ?: hashCode(),
        title = title,
        thumbnail = thumbnail
    )
