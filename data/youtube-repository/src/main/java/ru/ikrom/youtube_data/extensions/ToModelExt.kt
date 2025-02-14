package ru.ikrom.youtube_data.extensions


import com.zionhuang.innertube.models.AlbumItem
import com.zionhuang.innertube.models.Artist
import com.zionhuang.innertube.models.ArtistItem
import com.zionhuang.innertube.models.PlaylistItem
import com.zionhuang.innertube.models.SongItem
import com.zionhuang.innertube.pages.AlbumPage
import com.zionhuang.innertube.pages.ArtistPage
import ru.ikrom.youtube_data.model.AlbumModel
import ru.ikrom.youtube_data.model.AlbumPageModel
import ru.ikrom.youtube_data.model.ArtistModel
import ru.ikrom.youtube_data.model.ArtistPageModel
import ru.ikrom.youtube_data.model.PlaylistModel
import ru.ikrom.youtube_data.model.TrackModel

internal fun AlbumItem.toAlbumModel(): AlbumModel {
    return AlbumModel(
        id = this.id,
        title = this.title,
        artists = this.artists?.map{
            it.toArtistModel()
        },
        thumbnail = this.thumbnail.resize(1024, 1024),
        year = this.year
    )
}

internal fun ArtistItem.toArtistModel(): ArtistModel {
    return ArtistModel(
        id = this.id,
        name = this.title,
        thumbnail = this.thumbnail
    )
}

internal fun Artist.toArtistModel(): ArtistModel {
    return ArtistModel(
        name = this.name,
        id = this.id
    )
}

internal fun PlaylistItem.toPlayListModel(): PlaylistModel {
    return PlaylistModel(
        id = id,
        title = title,
        artists = author?.toArtistModel(),
        thumbnail =  thumbnail
    )
}

internal fun SongItem.toTrackModel(): TrackModel? {
    return try {
        TrackModel(
            title = this.title,
            videoId = this.id,
            album = AlbumModel(
                id = this.album?.id ?: "unknown",
                title = this.album?.name ?: "unknown",
                artists = this.artists.map { artist ->
                    ArtistModel(
                        id = artist.id!!,
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

internal fun ArtistPage.toArtistPageModel(): ArtistPageModel {
    var tracks = mutableListOf<TrackModel>()
    var albums = mutableListOf<AlbumModel>()
    var singles = mutableListOf<AlbumModel>()
    var relatedPlaylists = mutableListOf<PlaylistModel>()
    var similar = mutableListOf<ArtistModel>()

    this.sections.forEach { section ->
        when (section.items.first()) {
            is SongItem -> {
                if (tracks.isEmpty()){
                    tracks = section.items.mapNotNull { (it as SongItem).toTrackModel() } as MutableList<TrackModel>
                }
            }
            is AlbumItem -> {
                if(section.items.size == 1){
                    singles = (section.items.map { (it as AlbumItem).toAlbumModel() }) as MutableList<AlbumModel>
                } else {
                    albums = (section.items.map { (it as AlbumItem).toAlbumModel() }) as MutableList<AlbumModel>
                }
            }
            is PlaylistItem -> relatedPlaylists = (section.items.map { (it as PlaylistItem).toPlayListModel() }) as MutableList<PlaylistModel>
            is ArtistItem -> similar = (section.items.map { (it as ArtistItem).toArtistModel() }) as MutableList<ArtistModel>
        }
    }

    return ArtistPageModel(
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

internal fun AlbumPage.toModel(): AlbumPageModel{
    return AlbumPageModel(
        albumInfo = album.toAlbumModel(),
        tracks = songs.mapNotNull { it.toTrackModel() }
    )
}