package ru.ikrom.youtube_data.extensions

import ru.ikrom.database.AlbumEntity
import ru.ikrom.database.ArtistEntity
import ru.ikrom.database.TrackEntity
import ru.ikrom.youtube_data.model.AlbumModel
import ru.ikrom.youtube_data.model.ArtistModel
import ru.ikrom.youtube_data.model.TrackModel

internal fun TrackEntity.toModel(): TrackModel {
    val artistModel = ArtistModel(
        id = artistId,
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

internal fun ArtistEntity.toModel(): ArtistModel {
    return ArtistModel(id, name, thumbnail)
}

internal fun AlbumEntity.toModel(): AlbumModel {
    return AlbumModel(
        id = this.id,
        title = this.title,
        artists = listOf(ArtistModel(this.artistId, this.artistNames)),
        thumbnail = this.thumbnail,
        year = this.year
    )
}

internal fun List<AlbumEntity>.toAlbumModels() = map{it.toModel()}
internal fun List<TrackEntity>.toTrackModels() = map{it.toModel()}
internal fun List<ArtistEntity>.toArtistModels() = map{it.toModel()}