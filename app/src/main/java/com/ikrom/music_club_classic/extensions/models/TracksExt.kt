package com.ikrom.music_club_classic.extensions.models

import com.ikrom.music_club_classic.ui.adapters.delegates.MenuHeaderDelegateItem
import com.ikrom.music_club_classic.ui.adapters.delegates.ThumbnailMediumItem
import com.ikrom.music_club_classic.ui.adapters.delegates.ThumbnailSmallItem
import ru.ikrom.youtube_data.model.TrackModel


fun TrackModel.toThumbnailSmallItem(): ThumbnailSmallItem {
    return ThumbnailSmallItem(
        id = this.videoId,
        title = this.title,
        subtitle = this.album.artists.getNames(),
        thumbnail = this.album.thumbnail,
    )
}


fun TrackModel.toMenuHeaderItem(): MenuHeaderDelegateItem {
    return MenuHeaderDelegateItem(
        title = this.title,
        subtitle = this.album.artists.getNames(),
        thumbnail = this.album.thumbnail,
    )
}

fun TrackModel.toThumbnailMediumItem(): ThumbnailMediumItem {
    return ThumbnailMediumItem(
        id = videoId,
        title = this.title,
        subtitle = this.album.artists.getNames(),
        thumbnail = this.album.thumbnail
    )
}