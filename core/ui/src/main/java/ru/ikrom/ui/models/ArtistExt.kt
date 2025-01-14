package ru.ikrom.ui.models

import ru.ikrom.ui.base_adapter.delegates.ThumbnailRoundedItem
import ru.ikrom.ui.base_adapter.delegates.ThumbnailRoundedSmallItem
import ru.ikrom.youtube_data.model.ArtistModel


fun ArtistModel.toThumbnailRoundedItem(): ThumbnailRoundedItem {
    return ThumbnailRoundedItem(
        id = this.id!!,
        title = this.name,
        thumbnail = this.thumbnail ?: "",
    )
}

fun ArtistModel.toThumbnailRoundedSmallItem(): ThumbnailRoundedSmallItem {
    return ThumbnailRoundedSmallItem(
        id = this.id!!,
        title = this.name,
        subtitle = "",
        thumbnail = this.thumbnail ?: "",
    )
}