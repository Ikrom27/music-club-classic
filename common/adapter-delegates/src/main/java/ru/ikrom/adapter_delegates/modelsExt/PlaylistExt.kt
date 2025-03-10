package ru.ikrom.adapter_delegates.modelsExt

import android.os.Bundle
import ru.ikrom.adapter_delegates.delegates.CardItem
import ru.ikrom.adapter_delegates.delegates.ThumbnailHeaderItem
import ru.ikrom.adapter_delegates.delegates.ThumbnailItemMediumItem
import ru.ikrom.adapter_delegates.delegates.ThumbnailMediumPlaylistItem
import ru.ikrom.youtube_data.model.ArtistModel
import ru.ikrom.youtube_data.model.PlaylistModel


fun Bundle.toPlaylistModel(): PlaylistModel {
    val id = getString("id") ?: ""
    val title = getString("title") ?: ""
    val thumbnail = getString("thumbnail") ?: ""
    val artistId = getString("artist_id") ?: ""
    val artistName = getString("artist_name") ?: ""
    val artistThumbnail = getString("artist_thumbnail") ?: ""
    val artist = if (artistId.isNotEmpty() && artistName.isNotEmpty()) {
        ArtistModel(artistId, artistName, artistThumbnail)
    } else {
        null
    }
    return PlaylistModel(id, title, artist, thumbnail)
}


fun PlaylistModel.toCardItem(): CardItem {
    return CardItem(
        id = id,
        title = title,
        subtitle = artists?.name ?: "unknown author ",
        thumbnail = thumbnail
    )
}

fun PlaylistModel.toThumbnailHeaderItem(): ThumbnailHeaderItem {
    return ThumbnailHeaderItem(
        title = title,
        subtitle = artists?.name ?: "unknown author",
        thumbnail = thumbnail
    )
}


fun PlaylistModel.toThumbnailMediumItem() : ThumbnailItemMediumItem {
    return ThumbnailItemMediumItem(
        id = id,
        title = title,
        subtitle = artists?.name ?: "",
        thumbnail = thumbnail,
    )
}

fun PlaylistModel.toThumbnailMediumPlaylistItem() : ThumbnailMediumPlaylistItem {
    return ThumbnailMediumPlaylistItem(
        id = id,
        title = title,
        subtitle = "",
        thumbnail = thumbnail,
    )
}