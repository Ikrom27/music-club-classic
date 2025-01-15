package ru.ikrom.ui.models

import ru.ikrom.ui.base_adapter.delegates.MenuHeaderDelegateItem
import ru.ikrom.ui.base_adapter.delegates.ThumbnailRoundedItem
import ru.ikrom.ui.base_adapter.delegates.ThumbnailRoundedSmallItem
import ru.ikrom.youtube_data.model.ArtistModel


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