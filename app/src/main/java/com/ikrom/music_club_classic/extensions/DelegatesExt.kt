package com.ikrom.music_club_classic.extensions

import com.ikrom.music_club_classic.data.model.Album
import com.ikrom.music_club_classic.data.model.PlayList
import com.ikrom.music_club_classic.data.model.Track
import com.ikrom.music_club_classic.ui.adapters.delegates.MediumTrackItem
import com.ikrom.music_club_classic.ui.adapters.delegates.ThumbnailHeaderItem
import com.ikrom.music_club_classic.ui.adapters.delegates.CardItem


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

fun List<Album>.toCardItems(
    onItemClick: (Album) -> Unit
): List<CardItem>{
    return this.map { album ->
        album.toCardItem {
            onItemClick(album)
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