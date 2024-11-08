package ru.ikrom.youtube_data.extensions

import ru.ikrom.database.TrackEntity
import ru.ikrom.youtube_data.model.AlbumModel
import ru.ikrom.youtube_data.model.ArtistModel
import ru.ikrom.youtube_data.model.TrackModel

fun TrackEntity.toModel(): TrackModel {
    val artistModel = ArtistModel(
        id = artistId.takeIf { it.isNotEmpty() },
        name = artist,
        thumbnail = null
    )

    val albumModel = AlbumModel(
        id = albumId,
        title = "",
        artists = listOf(artistModel),
        thumbnail = thumbnail,
        year = null
    )

    return TrackModel(
        title = title,
        videoId = id,
        album = albumModel,
        isFavorite = true
    )
}
