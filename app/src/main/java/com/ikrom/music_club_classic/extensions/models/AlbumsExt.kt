package com.ikrom.music_club_classic.extensions.models

import com.ikrom.innertube.models.AlbumItem
import com.ikrom.music_club_classic.data.model.Album
import com.ikrom.music_club_classic.extensions.resize
import com.ikrom.music_club_classic.ui.adapters.delegates.CardItem
import com.ikrom.music_club_classic.ui.adapters.delegates.ThumbnailHeaderItem

fun AlbumItem.toAlbum(): Album {
    return Album(
        id = this.id,
        title = this.title,
        artists = this.artists?.map{
            it.toArtist()
        },
        thumbnail = this.thumbnail.resize(1024, 1024),
        year = this.year
    )
}

fun Album.toThumbnailHeaderItem(
    onPlayClick: () -> Unit,
    onShuffleClick: () -> Unit
): ThumbnailHeaderItem {
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

fun List<Album>.albumCardItems(
    onItemClick: (Album) -> Unit
): List<CardItem>{
    return this.map { album ->
        album.toCardItem {
            onItemClick(album)
        }
    }
}