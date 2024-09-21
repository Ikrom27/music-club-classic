package com.ikrom.music_club_classic.extensions.models

import com.ikrom.music_club_classic.ui.adapters.delegates.MenuHeaderDelegateItem
import com.ikrom.music_club_classic.ui.adapters.delegates.ThumbnailMediumItem
import com.ikrom.music_club_classic.ui.adapters.delegates.ThumbnailSmallItem
import ru.ikrom.youtube_data.model.TrackModel


fun TrackModel.toThumbnailSmallItem(
    onItemClick: () -> Unit,
    onButtonClick: () -> Unit,
    onLongClick: () -> Unit = {}
): ThumbnailSmallItem {
    return ThumbnailSmallItem(
        title = this.title,
        subtitle = this.album.artists.getNames(),
        thumbnail = this.album.thumbnail,
        onClick = {onItemClick()},
        onLongClick = {onLongClick()},
        onButtonClick = {onButtonClick()}
    )
}

fun List<TrackModel>?.toThumbnailSmallItems(
    onItemClick: (TrackModel) -> Unit,
    onButtonClick: () -> Unit,
    onLongClick: () -> Unit = {}
): List<ThumbnailSmallItem> {
    return this?.map { it.toThumbnailSmallItem(
        onItemClick = { onItemClick(it) },
        onButtonClick = onButtonClick,
        onLongClick = onLongClick
    ) } ?: emptyList()
}

fun TrackModel.toMenuHeaderItem(
    onClick: () -> Unit
): MenuHeaderDelegateItem {
    return MenuHeaderDelegateItem(
        title = this.title,
        subtitle = this.album.artists.getNames(),
        thumbnail = this.album.thumbnail,
        onClick = {
            onClick()
        }
    )
}

fun TrackModel.toThumbnailMediumItem(
    onClick: () -> Unit,
    onLongClick: () -> Unit
): ThumbnailMediumItem {
    return ThumbnailMediumItem(
        title = this.title,
        subtitle = this.album.artists.getNames(),
        thumbnail = this.album.thumbnail,
        onClick = { onClick() },
        onLongClick = { onLongClick() }
    )
}

fun List<TrackModel>.toThumbnailMediumItems(
    onClick: (TrackModel) -> Unit,
    onLongClick: (TrackModel) -> Unit
): List<ThumbnailMediumItem> {
    return this.map { it.toThumbnailMediumItem(
        onClick = { onClick(it) },
        onLongClick = { onLongClick(it) }
    ) }
}