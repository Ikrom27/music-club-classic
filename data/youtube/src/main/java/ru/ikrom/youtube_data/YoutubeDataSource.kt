package ru.ikrom.youtube_data

import com.ikrom.innertube.YouTube
import com.ikrom.innertube.models.AlbumItem
import com.ikrom.innertube.models.SongItem
import com.ikrom.innertube.models.WatchEndpoint
import com.ikrom.innertube.pages.ArtistPage


class YoutubeDataSource: IMediaDataSource {
    private var continuation = ""
    override suspend fun getTracksByQuery(query: String): List<SongItem> {
        return YouTube.search(query, YouTube.SearchFilter.FILTER_SONG).items.map { it as SongItem }
    }

    override suspend fun getNewReleaseAlbums(): List<AlbumItem> {
        return YouTube.newReleaseAlbums()
    }

    override suspend fun getAlbumTracks(albumId: String): List<SongItem> {
        return YouTube.albumSongs(albumId)
    }

    override suspend fun getRadioTracks(id: String): List<SongItem> {
        return YouTube.next(WatchEndpoint(id), continuation).items
    }

    override suspend fun getPlaylistTracks(albumId: String): List<SongItem> {
        return YouTube.albumSongs(albumId)
    }

    override suspend fun getArtistData(artistId: String): ArtistPage {
        return YouTube.artist(artistId)
    }
}