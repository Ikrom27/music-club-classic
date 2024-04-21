package com.ikrom.music_club_classic.extensions.models

import com.ikrom.innertube.models.PlaylistItem
import com.ikrom.music_club_classic.data.model.Playlist
import com.ikrom.music_club_classic.ui.adapters.LibraryItem
import com.ikrom.music_club_classic.ui.adapters.delegates.CardItem
import com.ikrom.music_club_classic.ui.adapters.delegates.ThumbnailHeaderItem

fun PlaylistItem.toPlayList(): Playlist {
    return Playlist(
        id = id,
        title = title,
        artists = author?.toArtist(),
        thumbnail =  thumbnail
    )
}

fun Playlist.toCardItem(
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

fun Playlist.toThumbnailHeaderItem(
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

fun Playlist.toLibraryItem(
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

fun List<Playlist>.toLibraryItems(
    onButtonClick: () -> Unit,
    onItemClick: (Playlist) -> Unit
): List<LibraryItem>{
    return this.map { playlist ->
        playlist.toLibraryItem(
            onButtonClick = {onButtonClick()},
            onItemClick = {onItemClick(playlist)}
        )
    }
}




fun List<Playlist>.playlistCardItems(
    onItemClick: (Playlist) -> Unit
): List<CardItem>{
    return this.map { playlist ->
        playlist.toCardItem {
            onItemClick(playlist)
        }
    }
}