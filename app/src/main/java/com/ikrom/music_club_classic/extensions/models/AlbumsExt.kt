package com.ikrom.music_club_classic.extensions.models

import com.ikrom.music_club_classic.ui.adapters.delegates.CardItem
import com.ikrom.music_club_classic.ui.adapters.delegates.ThumbnailHeaderItem
import com.ikrom.music_club_classic.ui.adapters.delegates.ThumbnailLargeItem
import com.ikrom.music_club_classic.ui.adapters.delegates.ThumbnailMediumItem
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