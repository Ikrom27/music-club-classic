package ru.ikrom.ui.models

import ru.ikrom.ui.base_adapter.delegates.CardItem
import ru.ikrom.ui.base_adapter.delegates.ThumbnailHeaderItem
import ru.ikrom.ui.base_adapter.delegates.ThumbnailLargeItem
import ru.ikrom.ui.base_adapter.delegates.ThumbnailMediumItem
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

fun AlbumModel.toThumbnailMediumItem() : ThumbnailMediumItem {
    return ThumbnailMediumItem(
        id = this.id,
        title = this.title,
        subtitle = this.artists.getNames(),
        thumbnail = this.thumbnail
    )
}

fun AlbumModel.toThumbnailLargeItem() : ThumbnailLargeItem {
    return ThumbnailLargeItem(
        title = this.title,
        subtitle = this.year.toString(),
        thumbnail = this.thumbnail,
    )
}

fun List<AlbumModel>?.toThumbnailLargeItems(
): List<ThumbnailLargeItem>{
    return this?.map { it.toThumbnailLargeItem() } ?: emptyList()
}