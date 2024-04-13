package com.ikrom.music_club_classic.extensions

import com.ikrom.music_club_classic.data.model.Album
import com.ikrom.music_club_classic.data.model.PlayList
import com.ikrom.music_club_classic.data.model.Track
import com.ikrom.music_club_classic.ui.adapters.delegates.MediumTrackItem
import com.ikrom.music_club_classic.ui.adapters.delegates.ThumbnailHeaderItem
import com.ikrom.music_club_classic.ui.adapters.delegates.CardItem
import com.ikrom.music_club_classic.ui.adapters.delegates.LargeThumbnailItem


fun Track.toMediumTrackItem(
    onItemClick: () -> Unit,
    onButtonClick: () -> Unit,
): MediumTrackItem{
    return MediumTrackItem(
        title = this.title,
        subtitle = this.album.artists.getNames(),
        thumbnail = this.album.thumbnail,
        onItemClick = {onItemClick()},
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
        onItemClick = { onItemClick() }
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
        onItemClick = { onItemClick() }
    )
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

fun Track.toLargeThumbnailItem(): LargeThumbnailItem {
    return LargeThumbnailItem(
        title = this.title,
        subtitle = this.album.title,
        thumbnail = this.album.thumbnail
    )
}

fun List<Track>.toLargeThumbnailItems(): List<LargeThumbnailItem> {
    return this.map { it.toLargeThumbnailItem() }
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