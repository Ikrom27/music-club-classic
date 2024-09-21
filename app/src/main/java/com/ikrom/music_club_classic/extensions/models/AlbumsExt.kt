package com.ikrom.music_club_classic.extensions.models

import com.ikrom.music_club_classic.ui.adapters.delegates.CardItem
import com.ikrom.music_club_classic.ui.adapters.delegates.ThumbnailHeaderItem
import com.ikrom.music_club_classic.ui.adapters.delegates.ThumbnailLargeItem
import com.ikrom.music_club_classic.ui.adapters.delegates.ThumbnailMediumItem
import ru.ikrom.youtube_data.model.AlbumModel


fun AlbumModel.toThumbnailHeaderItem(
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

fun AlbumModel.toCardItem(
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

fun AlbumModel.toThumbnailMediumItem(
    onClick: () -> Unit,
    onLongClick: () -> Unit
) : ThumbnailMediumItem {
    return ThumbnailMediumItem(
        title = this.title,
        subtitle = this.artists.getNames(),
        thumbnail = this.thumbnail,
        onClick = onClick,
        onLongClick = onLongClick
    )
}

fun List<AlbumModel>?.toThumbnailMediumItems(
    onClick: () -> Unit,
    onLongClick: () -> Unit
): List<ThumbnailMediumItem>{
    return this?.map {
        it.toThumbnailMediumItem(
        onClick,
        onLongClick
    ) } ?: emptyList()
}

fun AlbumModel.toThumbnailLargeItem(
    onClick: () -> Unit,
    onLongClick: () -> Unit
) : ThumbnailLargeItem {
    return ThumbnailLargeItem(
        title = this.title,
        subtitle = this.year.toString(),
        thumbnail = this.thumbnail,
        onClick = onClick,
        onLongClick = onLongClick
    )
}

fun List<AlbumModel>?.toThumbnailLargeItems(
    onClick: () -> Unit,
    onLongClick: () -> Unit
): List<ThumbnailLargeItem>{
    return this?.map { it.toThumbnailLargeItem(
        onClick,
        onLongClick
    ) } ?: emptyList()
}

fun List<AlbumModel>.albumCardItems(
    onItemClick: (AlbumModel) -> Unit
): List<CardItem>{
    return this.map { album ->
        album.toCardItem {
            onItemClick(album)
        }
    }
}