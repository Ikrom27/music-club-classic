package ru.ikrom.adapter_delegates.modelsExt

import ru.ikrom.youtube_data.model.ArtistModel
import ru.ikrom.adapter_delegates.delegates.ThumbnailRoundedItem
import ru.ikrom.adapter_delegates.delegates.ThumbnailRoundedSmallItem
import ru.ikrom.adapter_delegates.delegates.MenuHeaderDelegateItem


fun ArtistModel.toThumbnailRoundedItem(): ThumbnailRoundedItem {
    return ThumbnailRoundedItem(
        id = id!!,
        title = name,
        thumbnail = thumbnail ?: "",
    )
}

fun ArtistModel.toThumbnailRoundedSmallItem(): ThumbnailRoundedSmallItem {
    return ThumbnailRoundedSmallItem(
        id = id!!,
        title = name,
        subtitle = "",
        thumbnail = thumbnail ?: "",
    )
}

fun ArtistModel.toMenuHeaderItem(isFavorite: Boolean) = MenuHeaderDelegateItem(
    id = id ?: "",
    title = name,
    subtitle = "",
    thumbnail = thumbnail ?: "",
    isFavorite = isFavorite
)