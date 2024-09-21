package com.ikrom.music_club_classic.extensions.models

import com.ikrom.music_club_classic.ui.adapters.delegates.ThumbnailRoundedItem
import ru.ikrom.youtube_data.model.ArtistModel


fun List<ArtistModel>?.getNames(): String {
    return this?.joinToString(", ") { it.name } ?: ""
}


fun ArtistModel.toThumbnailRoundedItem(
    onClick: () -> Unit
): ThumbnailRoundedItem {
    return ThumbnailRoundedItem(
        title = this.name,
        thumbnail = this.thumbnail ?: "",
        onClick = { onClick() }
    )
}

fun List<ArtistModel>?.toThumbnailRoundedItems(
    onClick: (ArtistModel) -> Unit): List<ThumbnailRoundedItem> {
    return this?.map { it.toThumbnailRoundedItem { onClick(it) } } ?: emptyList()
}