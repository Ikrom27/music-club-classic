package com.ikrom.music_club_classic.extensions.models

import android.os.Bundle
import com.ikrom.music_club_classic.ui.adapters.LibraryItem
import com.ikrom.music_club_classic.ui.adapters.delegates.CardItem
import com.ikrom.music_club_classic.ui.adapters.delegates.ThumbnailHeaderItem
import com.ikrom.music_club_classic.ui.adapters.delegates.ThumbnailMediumItem
import ru.ikrom.youtube_data.model.ArtistModel
import ru.ikrom.youtube_data.model.PlaylistModel


fun Bundle.toPlaylistModel(): PlaylistModel {
    val id = getString("id") ?: ""
    val title = getString("title") ?: ""
    val thumbnail = getString("thumbnail") ?: ""
    val artistId = getString("artist_id") ?: ""
    val artistName = getString("artist_name") ?: ""
    val artistThumbnail = getString("artist_thumbnail") ?: ""
    val artist = if (artistId.isNotEmpty() && artistName.isNotEmpty()) {
        ArtistModel(artistId, artistName, artistThumbnail)
    } else {
        null
    }
    return PlaylistModel(id, title, artist, thumbnail)
}


fun PlaylistModel.toCardItem(
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

fun PlaylistModel.toThumbnailHeaderItem(
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

fun PlaylistModel.toLibraryItem(
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

fun List<PlaylistModel>.toLibraryItems(
    onButtonClick: () -> Unit,
    onItemClick: (PlaylistModel) -> Unit
): List<LibraryItem>{
    return this.map { playlist ->
        playlist.toLibraryItem(
            onButtonClick = {onButtonClick()},
            onItemClick = {onItemClick(playlist)}
        )
    }
}

fun List<PlaylistModel>.playlistCardItems(
    onItemClick: (PlaylistModel) -> Unit
): List<CardItem>{
    return this.map { playlist ->
        playlist.toCardItem {
            onItemClick(playlist)
        }
    }
}

fun PlaylistModel.toThumbnailMediumItem(
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

fun List<PlaylistModel>?.toThumbnailMediumItems(
    onClick: () -> Unit,
    onLongClick: () -> Unit
): List<ThumbnailMediumItem>{
    return this?.map { it.toThumbnailMediumItem(
        onClick,
        onLongClick
    ) } ?: emptyList()
}