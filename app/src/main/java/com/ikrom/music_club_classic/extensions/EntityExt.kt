package com.ikrom.music_club_classic.extensions

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
