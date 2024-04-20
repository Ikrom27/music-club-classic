package com.ikrom.music_club_classic.extensions

import com.ikrom.innertube.models.AlbumItem
import com.ikrom.innertube.models.ArtistItem
import com.ikrom.innertube.models.ItemArtist
import com.ikrom.innertube.models.PlaylistItem
import com.ikrom.innertube.models.SongItem
import com.ikrom.innertube.pages.ArtistPage
import com.ikrom.innertube.pages.YTArtistSection
import com.ikrom.music_club_classic.data.model.Album
import com.ikrom.music_club_classic.data.model.Artist
import com.ikrom.music_club_classic.data.model.ArtistData
import com.ikrom.music_club_classic.data.model.ArtistSection
import com.ikrom.music_club_classic.data.model.PlayList
import com.ikrom.music_club_classic.data.model.Track


fun String.resize(
    width: Int? = null,
    height: Int? = null,
): String {
    if (width == null && height == null) return this
    "https://lh3\\.googleusercontent\\.com/.*=w(\\d+)-h(\\d+).*".toRegex().matchEntire(this)?.groupValues?.let { group ->
        val (W, H) = group.drop(1).map { it.toInt() }
        var w = width
        var h = height
        if (w != null && h == null) h = (w / W) * H
        if (w == null && h != null) w = (h / H) * W
        return "${split("=w")[0]}=w$w-h$h-p-l90-rj"
    }
    if (this matches "https://yt3\\.ggpht\\.com/.*=s(\\d+)".toRegex()) {
        return "$this-s${width ?: height}"
    }
    return this
}


fun SongItem.toTrack(): Track? {
    return try {
        Track(
            title = this.title,
            videoId = this.id,
            album = Album(
                id = this.album?.id ?: "null album",
                title = this.album?.name ?: "null title",
                artists = this.artists.map { artist ->
                    Artist(
                        id = artist.id,
                        name = artist.name
                    )
                },
                thumbnail = this.thumbnail.resize(1024, 1024),
                year = null
            )
        )
    } catch (e: NullPointerException) {
        null
    }
}



fun ItemArtist.toArtist(): Artist {
    return Artist(
        name = this.name,
        id = this.id
    )
}


fun AlbumItem.toAlbum(): Album {
    return Album(
        id = this.id,
        title = this.title,
        artists = this.artists?.map{
            it.toArtist()
                                    },
        thumbnail = this.thumbnail.resize(1024, 1024),
        year = this.year
    )
}

fun PlaylistItem.toPlayList(): PlayList {
    return PlayList(
        id = id,
        title = title,
        artists = author?.toArtist(),
        thumbnail =  thumbnail
    )
}

fun List<Artist>?.getNames(): String {
    return this?.joinToString(", ") { it.name } ?: ""
}

fun ArtistItem.toArtist(): Artist {
    return Artist(
        id = this.id,
        name = this.title,
        thumbnail = this.thumbnail
    )
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
        relatedPlayLists = (this.sections[4].items.mapNotNull { (it as? PlaylistItem)?.toPlayList()  } ),
        similar = (this.sections[5].items.mapNotNull { (it as? ArtistItem)?.toArtist() } )
    )
}