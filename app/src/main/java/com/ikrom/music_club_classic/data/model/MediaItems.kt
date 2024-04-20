package com.ikrom.music_club_classic.data.model


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
    val name: String,
    val thumbnail: String? = null
)

data class PlayList(
    val id: String,
    val title: String,
    val artists: Artist?,
    val thumbnail: String,
)

data class ArtistData(
    val id: String,
    val title: String,
    val thumbnail: String,
    val description: String,
    val tracks: List<Track>?,
    val albums: List<Album>?,
    val singles: List<Album>?,
    val relatedPlayLists: List<PlayList>?,
    val similar: List<Artist>?
)

data class ArtistSection(
    val title: String,
    val items: List<Any>
)