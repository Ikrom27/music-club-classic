package com.ikrom.music_club_classic.extensions.models

import com.ikrom.innertube.models.AlbumItem
import com.ikrom.innertube.models.ArtistItem
import com.ikrom.innertube.models.ItemArtist
import com.ikrom.innertube.models.PlaylistItem
import com.ikrom.innertube.models.SongItem
import com.ikrom.innertube.pages.ArtistPage
import com.ikrom.music_club_classic.data.model.Artist
import com.ikrom.music_club_classic.data.model.ArtistData


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
    return ArtistData(
        id = this.artist.id,
        title = this.artist.title,
        thumbnail = this.artist.thumbnail,
        description = this.description ?: "",
        tracks = (this.sections[0].items.mapNotNull { (it as? SongItem)?.toTrack() } ),
        albums = (this.sections[1].items.mapNotNull { (it as? AlbumItem)?.toAlbum() } ),
        singles = (this.sections[2].items.mapNotNull { (it as? AlbumItem)?.toAlbum() } ),
        relatedPlaylists = (this.sections[4].items.mapNotNull { (it as? PlaylistItem)?.toPlayList()  } ),
        similar = (this.sections[5].items.mapNotNull { (it as? ArtistItem)?.toArtist() } )
    )
}