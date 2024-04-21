package com.ikrom.music_club_classic.extensions.models

import com.ikrom.innertube.models.SongItem
import com.ikrom.music_club_classic.data.model.Album
import com.ikrom.music_club_classic.data.model.Artist
import com.ikrom.music_club_classic.data.model.Track
import com.ikrom.music_club_classic.extensions.resize
import com.ikrom.music_club_classic.ui.adapters.delegates.MediumPlusThumbnailItem
import com.ikrom.music_club_classic.ui.adapters.delegates.MediumTrackItem
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

fun Track.toMediumTrackItem(
    onItemClick: () -> Unit,
    onButtonClick: () -> Unit,
): MediumTrackItem {
    return MediumTrackItem(
        title = this.title,
        subtitle = this.album.artists.getNames(),
        thumbnail = this.album.thumbnail,
        onClick = {onItemClick()},
        onButtonClick = {onButtonClick()}
    )
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

fun Track.toLargeThumbnailItem(
    onClick: () -> Unit,
    onLongClick: () -> Unit
): MediumPlusThumbnailItem {
    return MediumPlusThumbnailItem(
        title = this.title,
        subtitle = this.album.artists.getNames(),
        thumbnail = this.album.thumbnail,
        onClick = { onClick() },
        onLongClick = { onLongClick() }
    )
}

fun List<Track>.toMediumPlusThumbnailItems(
    onClick: (Track) -> Unit,
    onLongClick: (Track) -> Unit
): List<MediumPlusThumbnailItem> {
    return this.map { it.toLargeThumbnailItem(
        onClick = { onClick(it) },
        onLongClick = { onLongClick(it) }
    ) }
}