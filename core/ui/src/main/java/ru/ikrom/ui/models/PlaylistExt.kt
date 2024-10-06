package ru.ikrom.ui.models

import android.os.Bundle
import ru.ikrom.ui.base_adapter.delegates.CardItem
import ru.ikrom.ui.base_adapter.delegates.LibraryItem
import ru.ikrom.ui.base_adapter.delegates.ThumbnailHeaderItem
import ru.ikrom.ui.base_adapter.delegates.ThumbnailItemMediumItem
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

fun PlaylistModel.toLibraryItem(): LibraryItem {
    return LibraryItem(
        id = id,
        title = title,
        subtitle = artists?.name ?: "unknown author ",
        thumbnail = thumbnail,
    )
}

fun PlaylistModel.toThumbnailMediumItem() : ThumbnailItemMediumItem {
    return ThumbnailItemMediumItem(
        id = id,
        title = this.title,
        subtitle = this.artists?.name ?: "",
        thumbnail = this.thumbnail,
    )
}