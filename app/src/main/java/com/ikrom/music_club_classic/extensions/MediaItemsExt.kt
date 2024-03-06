package com.ikrom.music_club_classic.extensions

import com.ikrom.music_club_classic.data.model.Track
import com.ikrom.music_club_classic.data.model.TrackEntity


fun Track.toTrackEntity(): TrackEntity {
    return TrackEntity(
        videoId = this.videoId,
        title = this.title,
        albumId = this.album.id,
        albumTitle = this.album.title,
        cover = this.album.thumbnail,
        year = this.album.year,
        artistId = this.album.artists?.firstOrNull()?.id.orEmpty(),
        artistName = this.album.artists?.firstOrNull()?.name.orEmpty()
    )
}
