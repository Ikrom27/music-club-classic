package ru.ikrom.youtube_data

import android.util.Log
import com.zionhuang.innertube.YouTube
import com.zionhuang.innertube.models.AlbumItem
import com.zionhuang.innertube.models.SongItem
import com.zionhuang.innertube.models.WatchEndpoint
import com.zionhuang.innertube.pages.AlbumPage
import com.zionhuang.innertube.pages.ArtistPage


class YoutubeDataSource: IMediaDataSource {
    private var continuation = ""
    override suspend fun getTracksByQuery(query: String): List<SongItem> {
        return YouTube.search(query, YouTube.SearchFilter.FILTER_SONG).items.map { it as SongItem }
    }

    override suspend fun getNewReleaseAlbums(): List<AlbumItem> {
        return YouTube.newReleaseAlbums()
    }

    override suspend fun getAlbumTracks(albumId: String): List<SongItem> {
        return YouTube.album(albumId).songs
    }

    override suspend fun getRadioTracks(id: String): List<SongItem> {
        runCatching {
            YouTube.next(WatchEndpoint(id), continuation)
        }.onSuccess { result ->
            continuation = result.continuation ?: ""
            return result.items
        }.onFailure {
            Log.e(TAG, "get RadioTracks failure")
        }
        return emptyList()
    }

    override suspend fun getPlaylistTracks(albumId: String): List<SongItem> {
        return YouTube.albumSongs(albumId)
    }

    override suspend fun getArtistData(artistId: String): ArtistPage {
        return YouTube.artist(artistId)
    }

    override suspend fun getAlbumPage(id: String): AlbumPage {
        return YouTube.album(id)
    }

    companion object {
        val TAG = YoutubeDataSource::class.simpleName
    }
}