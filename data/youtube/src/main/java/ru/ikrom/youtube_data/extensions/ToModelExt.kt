package ru.ikrom.youtube_data.extensions

import com.ikrom.innertube.models.AlbumItem
import com.ikrom.innertube.models.ArtistItem
import com.ikrom.innertube.models.ItemArtist
import com.ikrom.innertube.models.PlaylistItem
import com.ikrom.innertube.models.SongItem
import ru.ikrom.youtube_data.model.AlbumModel
import ru.ikrom.youtube_data.model.ArtistModel
import ru.ikrom.youtube_data.model.PlaylistModel
import ru.ikrom.youtube_data.model.TrackModel

fun AlbumItem.toAlbumModel(): AlbumModel {
    return AlbumModel(
        id = this.id,
        title = this.title,
        artists = this.artists?.map{
            it.toArtistModel()
        },
        thumbnail = this.thumbnail.resize(1024, 1024),
        year = this.year
    )
}

fun ArtistItem.toArtistModel(): ArtistModel {
    return ArtistModel(
        id = this.id,
        name = this.title,
        thumbnail = this.thumbnail
    )
}

fun ItemArtist.toArtistModel(): ArtistModel {
    return ArtistModel(
        name = this.name,
        id = this.id
    )
}

fun PlaylistItem.toPlayListModel(): PlaylistModel {
    return PlaylistModel(
        id = id,
        title = title,
        artists = author?.toArtistModel(),
        thumbnail =  thumbnail
    )
}

fun SongItem.toTrackModel(): TrackModel? {
    return try {
        TrackModel(
            title = this.title,
            videoId = this.id,
            album = AlbumModel(
                id = this.album?.id ?: "unknown",
                title = this.album?.name ?: "unknown",
                artists = this.artists.map { artist ->
                    ArtistModel(
                        id = artist.id,
                        name = artist.name
                    )
                },
                thumbnail = this.thumbnail.resize(1024, 1024),
                year = null
            )
        )
    } catch (e: NullPointerException) {
        null
    }
}