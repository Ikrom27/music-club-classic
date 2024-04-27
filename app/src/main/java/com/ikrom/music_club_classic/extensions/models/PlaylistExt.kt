package com.ikrom.music_club_classic.extensions.models

import android.os.Bundle
import com.ikrom.innertube.models.PlaylistItem
import com.ikrom.music_club_classic.data.model.Album
import com.ikrom.music_club_classic.data.model.Artist
import com.ikrom.music_club_classic.data.model.Playlist
import com.ikrom.music_club_classic.ui.adapters.LibraryItem
import com.ikrom.music_club_classic.ui.adapters.delegates.CardItem
import com.ikrom.music_club_classic.ui.adapters.delegates.ThumbnailHeaderItem
import com.ikrom.music_club_classic.ui.adapters.delegates.ThumbnailMediumItem
import com.ikrom.music_club_classic.ui.adapters.delegates.ThumbnailRoundedItem

fun PlaylistItem.toPlayList(): Playlist {
    return Playlist(
        id = id,
        title = title,
        artists = author?.toArtist(),
        thumbnail =  thumbnail
    )
}

fun Bundle.toPlaylist(): Playlist {
    val id = getString("id") ?: ""
    val title = getString("title") ?: ""
    val thumbnail = getString("thumbnail") ?: ""
    val artistId = getString("artist_id") ?: ""
    val artistName = getString("artist_name") ?: ""
    val artistThumbnail = getString("artist_thumbnail") ?: ""
    val artist = if (artistId.isNotEmpty() && artistName.isNotEmpty()) {
        Artist(artistId, artistName, artistThumbnail)
    } else {
        null
    }
    return Playlist(id, title, artist, thumbnail)
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

fun Playlist.toThumbnailMediumItem(
    onClick: () -> Unit,
    onLongClick: () -> Unit
) : ThumbnailMediumItem {
    return ThumbnailMediumItem(
        title = this.title,
        subtitle = this.artists?.name ?: "",
        thumbnail = this.thumbnail,
        onClick = onClick,
        onLongClick = onLongClick
    )
}

fun List<Playlist>?.toThumbnailMediumItems(
    onClick: () -> Unit,
    onLongClick: () -> Unit
): List<ThumbnailMediumItem>{
    return this?.map { it.toThumbnailMediumItem(
        onClick,
        onLongClick
    ) } ?: emptyList()
}