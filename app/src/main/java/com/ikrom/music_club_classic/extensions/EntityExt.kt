package com.ikrom.music_club_classic.extensions

import androidx.media3.common.MediaItem
import com.ikrom.music_club_classic.data.model.Album
import com.ikrom.music_club_classic.data.model.Artist
import com.ikrom.music_club_classic.data.model.Track
import com.ikrom.music_club_classic.data.model.TrackEntity



fun TrackEntity.toTrack(): Track {
    return Track(
        title = this.title,
        videoId = this.videoId,
        album = Album(
            this.albumId,
            this.albumTitle,
            listOf(Artist(
                this.artistId,
                this.artistName
            )),
            this.cover,
            this.year
        )
    )
}

fun MediaItem.toTrackEntity(
    albumId: String,
    artistId: String,
): TrackEntity {
    return TrackEntity(
        videoId = this.mediaId,
        title = this.mediaMetadata.title.toString(),
        albumId = albumId,
        albumTitle = this.mediaMetadata.albumTitle.toString(),
        cover = this.mediaMetadata.artworkUri.toString(),
        year = this.mediaMetadata.releaseYear,
        artistId = artistId,
        artistName = this.mediaMetadata.artist.toString()
    )
}

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