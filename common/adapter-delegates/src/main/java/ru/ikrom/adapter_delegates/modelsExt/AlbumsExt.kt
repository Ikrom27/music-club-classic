package ru.ikrom.adapter_delegates.modelsExt

import ru.ikrom.adapter_delegates.delegates.CardItem
import ru.ikrom.adapter_delegates.delegates.MenuHeaderDelegateItem
import ru.ikrom.adapter_delegates.delegates.ThumbnailHeaderItem
import ru.ikrom.adapter_delegates.delegates.ThumbnailLargeItem
import ru.ikrom.adapter_delegates.delegates.ThumbnailItemMediumItem
import ru.ikrom.adapter_delegates.delegates.ThumbnailMediumPlaylistItem
import ru.ikrom.youtube_data.model.AlbumModel
import ru.ikrom.youtube_data.model.getNames


fun AlbumModel.toThumbnailHeaderItem(): ThumbnailHeaderItem {
    return ThumbnailHeaderItem(
        title = title,
        subtitle = artists.getNames(),
        thumbnail = thumbnail
    )
}

fun AlbumModel.toCardItem(): CardItem {
    return CardItem(
        id = id,
        title = title,
        subtitle = artists.getNames(),
        thumbnail = thumbnail
    )
}

fun AlbumModel.toThumbnailLargeItem() : ThumbnailLargeItem {
    return ThumbnailLargeItem(
        id = id,
        title = title,
        subtitle = year.toString(),
        thumbnail = thumbnail,
    )
}

fun AlbumModel.toThumbnailMediumPlaylistItem() : ThumbnailMediumPlaylistItem {
    return ThumbnailMediumPlaylistItem(
        id = id,
        title = title,
        subtitle = year.toString(),
        thumbnail = thumbnail,
    )
}

fun AlbumModel.toMenuHeaderItem(isFavorite: Boolean) = MenuHeaderDelegateItem(
    id = id,
    title = title,
    subtitle = artists?.joinToString(", ") { it.name } ?: "",
    thumbnail = thumbnail,
    isFavorite = isFavorite
)

