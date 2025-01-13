package ru.ikrom.youtube_data.model


data class TrackModel(
    val title: String,
    val videoId: String,
    val album: AlbumModel,
    val isFavorite: Boolean = false
) {
    val shareLink: String
        get() = "https://music.youtube.com/watch?v=$videoId"
}

data class AlbumModel(
    val id: String,
    val title: String,
    val artists: List<ArtistModel>?,
    val thumbnail: String,
    val year: Int?
) {
    val shareLink: String
        get() = "https://music.youtube.com/playlist?list=$id"
}

data class AlbumPageModel(
    val albumInfo: AlbumModel,
    val tracks: List<TrackModel>
)

data class ArtistModel(
    val id: String?,
    val name: String,
    val thumbnail: String? = null
)

data class PlaylistModel(
    val id: String,
    val title: String,
    val artists: ArtistModel?,
    val thumbnail: String,
)

data class ArtistPageModel(
    val id: String,
    val title: String,
    val thumbnail: String,
    val description: String,
    val tracks: List<TrackModel>,
    val albums: List<AlbumModel>,
    val singles: List<AlbumModel>,
    val relatedPlaylists: List<PlaylistModel>,
    val similar: List<ArtistModel>
)

data class ArtistSectionModel(
    val title: String,
    val items: List<Any>
)

fun List<ArtistModel>?.getNames(): String {
    return this?.joinToString(", ") { it.name } ?: ""
}