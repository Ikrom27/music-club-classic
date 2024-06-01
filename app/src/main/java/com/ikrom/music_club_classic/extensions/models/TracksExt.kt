package com.ikrom.music_club_classic.extensions.models

import com.ikrom.innertube.models.SongItem
import com.ikrom.music_club_classic.data.model.Album
import com.ikrom.music_club_classic.data.model.Artist
import com.ikrom.music_club_classic.data.model.Track
import com.ikrom.music_club_classic.extensions.resize
import com.ikrom.music_club_classic.ui.adapters.delegates.ThumbnailMediumItem
import com.ikrom.music_club_classic.ui.adapters.delegates.ThumbnailSmallItem
import com.ikrom.music_club_classic.ui.adapters.delegates.MenuHeaderDelegateItem

fun SongItem.toTrack(): Track? {
    return try {
        Track(
            title = this.title,
            videoId = this.id,
            album = Album(
                id = this.album?.id ?: "null album",
                title = this.album?.name ?: "null title",
                artists = this.artists.map { artist ->
                    Artist(
                        id = artist.id,
                        name = artist.name
                    )
                },
                thumbnail = this.thumbnail.resize(1024, 1024),
                year = null
            )
        )
    } catch (e: NullPointerException) {
        null
    }
}

fun Track.toThumbnailSmallItem(
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

fun List<Track>?.toThumbnailSmallItems(
    onItemClick: (Track) -> Unit,
    onButtonClick: () -> Unit,
    onLongClick: () -> Unit = {}
): List<ThumbnailSmallItem> {
    return this?.map { it.toThumbnailSmallItem(
        onItemClick = { onItemClick(it) },
        onButtonClick = onButtonClick,
        onLongClick = onLongClick
    ) } ?: emptyList()
}

fun Track.toMenuHeaderItem(
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

fun Track.toThumbnailMediumItem(
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

fun List<Track>.toThumbnailMediumItems(
    onClick: (Track) -> Unit,
    onLongClick: (Track) -> Unit
): List<ThumbnailMediumItem> {
    return this.map { it.toThumbnailMediumItem(
        onClick = { onClick(it) },
        onLongClick = { onLongClick(it) }
    ) }
}