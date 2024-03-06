package com.ikrom.music_club_classic.data.model

import com.ikrom.innertube.models.ItemArtist


data class Track(
    val title: String,
    val videoId: String,
    val album: Album,
    val isFavorite: Boolean = false
)

data class Album(
    val id: String,
    val title: String,
    val artists: List<Artist>?,
    val thumbnail: String,
    val year: Int?
) {
    val shareLink: String
        get() = "https://music.youtube.com/playlist?list=$id"
}

data class Artist(
    val id: String?,
    val name: String
)

data class PlayList(
    val id: String,
    val title: String,
    val author: Artist?,
    val thumbnail: String,
)