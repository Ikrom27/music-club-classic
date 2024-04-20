package com.ikrom.music_club_classic.extensions

import com.ikrom.music_club_classic.data.model.Album
import com.ikrom.music_club_classic.data.model.PlayList
import com.ikrom.music_club_classic.data.model.Track
import com.ikrom.music_club_classic.ui.adapters.LibraryItem
import com.ikrom.music_club_classic.ui.adapters.delegates.MenuHeaderDelegateItem
import com.ikrom.music_club_classic.ui.adapters.delegates.MediumTrackItem
import com.ikrom.music_club_classic.ui.adapters.delegates.ThumbnailHeaderItem
import com.ikrom.music_club_classic.ui.adapters.delegates.CardItem
import com.ikrom.music_club_classic.ui.adapters.delegates.MediumPlusThumbnailItem


fun Track.toMediumTrackItem(
    onItemClick: () -> Unit,
    onButtonClick: () -> Unit,
): MediumTrackItem{
    return MediumTrackItem(
        title = this.title,
        subtitle = this.album.artists.getNames(),
        thumbnail = this.album.thumbnail,
        onClick = {onItemClick()},
        onButtonClick = {onButtonClick()}
    )
}

fun Album.toThumbnailHeaderItem(
    onPlayClick: () -> Unit,
    onShuffleClick: () -> Unit
): ThumbnailHeaderItem{
    return ThumbnailHeaderItem(
        title = title,
        subtitle = artists.getNames(),
        thumbnail = thumbnail,
        onPlayClick = { onPlayClick() },
        onShuffleClick = { onShuffleClick() }
    )
}

fun Album.toCardItem(
    onItemClick: () -> Unit
)
: CardItem {
    return CardItem(
        title = title,
        subtitle = artists.getNames(),
        thumbnail = thumbnail,
        onClick = { onItemClick() }
    )
}

fun PlayList.toCardItem(
    onItemClick: () -> Unit
)
        : CardItem {
    return CardItem(
        title = title,
        subtitle = artists?.name ?: "unknown author ",
        thumbnail = thumbnail,
        onClick = { onItemClick() }
    )
}

fun PlayList.toLibraryItem(
    onButtonClick: () -> Unit,
    onItemClick: () -> Unit
)
        : LibraryItem {
    return LibraryItem(
        title = title,
        subtitle = artists?.name ?: "unknown author ",
        thumbnail = thumbnail,
        onLongClick = {onButtonClick()},
        onClick = { onItemClick() }
    )
}

fun List<PlayList>.toLibraryItems(
    onButtonClick: () -> Unit,
    onItemClick: (PlayList) -> Unit
): List<LibraryItem>{
    return this.map { playlist ->
        playlist.toLibraryItem(
            onButtonClick = {onButtonClick()},
            onItemClick = {onItemClick(playlist)}
        )
    }
}

fun List<Album>.albumCardItems(
    onItemClick: (Album) -> Unit
): List<CardItem>{
    return this.map { album ->
        album.toCardItem {
            onItemClick(album)
        }
    }
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

fun List<PlayList>.playlistCardItems(
    onItemClick: (PlayList) -> Unit
): List<CardItem>{
    return this.map { playlist ->
        playlist.toCardItem {
            onItemClick(playlist)
        }
    }
}

fun PlayList.toThumbnailHeaderItem(
    onPlayClick: () -> Unit,
    onShuffleClick: () -> Unit
): ThumbnailHeaderItem{
    return ThumbnailHeaderItem(
        title = title,
        subtitle = artists?.name ?: "unknown author",
        thumbnail = thumbnail,
        onPlayClick = { onPlayClick() },
        onShuffleClick = { onShuffleClick() }
    )
}