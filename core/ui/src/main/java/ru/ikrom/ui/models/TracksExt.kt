package ru.ikrom.ui.models

import ru.ikrom.ui.base_adapter.delegates.MenuHeaderDelegateItem
import ru.ikrom.ui.base_adapter.delegates.ThumbnailItemGrid
import ru.ikrom.ui.base_adapter.delegates.ThumbnailItemMediumItem
import ru.ikrom.ui.base_adapter.delegates.ThumbnailSmallItem
import ru.ikrom.youtube_data.model.TrackModel
import ru.ikrom.youtube_data.model.getNames


fun TrackModel.toThumbnailSmallItem(): ThumbnailSmallItem {
    return ThumbnailSmallItem(
        id = this.videoId,
        title = this.title,
        subtitle = this.album.artists.getNames(),
        thumbnail = this.album.thumbnail,
    )
}


fun TrackModel.toMenuHeaderItem(isFavorite: Boolean): MenuHeaderDelegateItem {
    return MenuHeaderDelegateItem(
        title = this.title,
        subtitle = this.album.artists.getNames(),
        thumbnail = this.album.thumbnail,
        isFavorite = isFavorite,
    )
}

fun TrackModel.toThumbnailMediumItem(): ThumbnailItemMediumItem {
    return ThumbnailItemMediumItem(
        id = videoId,
        title = this.title,
        subtitle = this.album.artists.getNames(),
        thumbnail = this.album.thumbnail
    )
}

fun TrackModel.toGridItem(): ThumbnailItemGrid {
    return ThumbnailItemGrid(
        id = videoId,
        title = this.title,
        subtitle = this.album.artists.getNames(),
        thumbnail = this.album.thumbnail
    )
}