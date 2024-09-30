package com.ikrom.music_club_classic.extensions.models

import ru.ikrom.ui.base_adapter.delegates.ThumbnailRoundedItem
import ru.ikrom.youtube_data.model.ArtistModel


fun ArtistModel.toThumbnailRoundedItem(): ThumbnailRoundedItem {
    return ThumbnailRoundedItem(
        id = this.id!!,
        title = this.name,
        thumbnail = this.thumbnail ?: "",
    )
}