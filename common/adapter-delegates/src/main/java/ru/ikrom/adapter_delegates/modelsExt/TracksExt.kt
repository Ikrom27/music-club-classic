package ru.ikrom.adapter_delegates.modelsExt

import ru.ikrom.adapter_delegates.delegates.MenuHeaderDelegateItem
import ru.ikrom.adapter_delegates.delegates.ThumbnailItemGrid
import ru.ikrom.adapter_delegates.delegates.ThumbnailItemMediumItem
import ru.ikrom.adapter_delegates.delegates.ThumbnailSmallItem
import ru.ikrom.youtube_data.model.TrackModel
import ru.ikrom.youtube_data.model.getNames


fun TrackModel.toThumbnailSmallItem(): ThumbnailSmallItem {
    return ThumbnailSmallItem(
        id = videoId,
        title = title,
        subtitle = album.artists.getNames(),
        thumbnail = album.thumbnail,
    )
}


fun TrackModel.toMenuHeaderItem(isFavorite: Boolean): MenuHeaderDelegateItem {
    return MenuHeaderDelegateItem(
        id = videoId,
        title = title,
        subtitle = album.artists.getNames(),
        thumbnail = album.thumbnail,
        isFavorite = isFavorite,
    )
}

fun TrackModel.toThumbnailMediumItem(): ThumbnailItemMediumItem {
    return ThumbnailItemMediumItem(
        id = videoId,
        title = title,
        subtitle = album.artists.getNames(),
        thumbnail = album.thumbnail
    )
}

fun TrackModel.toGridItem(): ThumbnailItemGrid {
    return ThumbnailItemGrid(
        id = videoId,
        title = title,
        subtitle = album.artists.getNames(),
        thumbnail = album.thumbnail
    )
}