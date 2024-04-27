package com.ikrom.music_club_classic.extensions.models

import com.ikrom.innertube.models.AlbumItem
import com.ikrom.innertube.models.ArtistItem
import com.ikrom.innertube.models.ItemArtist
import com.ikrom.innertube.models.PlaylistItem
import com.ikrom.innertube.models.SongItem
import com.ikrom.innertube.pages.ArtistPage
import com.ikrom.music_club_classic.data.model.Album
import com.ikrom.music_club_classic.data.model.Artist
import com.ikrom.music_club_classic.data.model.ArtistData
import com.ikrom.music_club_classic.data.model.Playlist
import com.ikrom.music_club_classic.data.model.Track
import com.ikrom.music_club_classic.ui.adapters.delegates.ThumbnailRoundedItem


fun ArtistItem.toArtist(): Artist {
    return Artist(
        id = this.id,
        name = this.title,
        thumbnail = this.thumbnail
    )
}

fun ItemArtist.toArtist(): Artist {
    return Artist(
        name = this.name,
        id = this.id
    )
}

fun List<Artist>?.getNames(): String {
    return this?.joinToString(", ") { it.name } ?: ""
}

fun ArtistPage.toArtistData(): ArtistData {
    var tracks = mutableListOf<Track>()
    var albums = mutableListOf<Album>()
    var singles = mutableListOf<Album>()
    var relatedPlaylists = mutableListOf<Playlist>()
    var similar = mutableListOf<Artist>()

    this.sections.forEach { section ->
        when (section.items.first()) {
            is SongItem -> {
                if (tracks.isEmpty()){
                    tracks = section.items.mapNotNull { (it as SongItem).toTrack() } as MutableList<Track>
                }
            }
            is AlbumItem -> {
                if(section.items.size == 1){
                    singles = (section.items.map { (it as AlbumItem).toAlbum() }) as MutableList<Album>
                } else {
                    albums = (section.items.map { (it as AlbumItem).toAlbum() }) as MutableList<Album>
                }
            }
            is PlaylistItem -> relatedPlaylists = (section.items.map { (it as PlaylistItem).toPlayList() }) as MutableList<Playlist>
            is ArtistItem -> similar = (section.items.map { (it as ArtistItem).toArtist() }) as MutableList<Artist>
        }
    }

    return ArtistData(
        id = this.artist.id,
        title = this.artist.title,
        thumbnail = this.artist.thumbnail,
        description = this.description ?: "",
        tracks = tracks,
        albums = albums,
        singles = singles,
        relatedPlaylists = relatedPlaylists,
        similar = similar
    )
}


fun Artist.toThumbnailRoundedItem(
    onClick: () -> Unit
): ThumbnailRoundedItem {
    return ThumbnailRoundedItem(
        title = this.name,
        thumbnail = this.thumbnail ?: "",
        onClick = { onClick() }
    )
}

fun List<Artist>?.toThumbnailRoundedItems(
    onClick: (Artist) -> Unit): List<ThumbnailRoundedItem> {
    return this?.map { it.toThumbnailRoundedItem { onClick(it) } } ?: emptyList()
}