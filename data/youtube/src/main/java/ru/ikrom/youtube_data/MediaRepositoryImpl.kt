package ru.ikrom.youtube_data

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import ru.ikrom.database.ILocalDataSource
import ru.ikrom.youtube_data.extensions.toAlbumModel
import ru.ikrom.youtube_data.extensions.toArtistPageModel
import ru.ikrom.youtube_data.extensions.toEntity
import ru.ikrom.youtube_data.extensions.toModel
import ru.ikrom.youtube_data.extensions.toTrackModel
import ru.ikrom.youtube_data.model.AlbumModel
import ru.ikrom.youtube_data.model.AlbumPageModel
import ru.ikrom.youtube_data.model.ArtistPageModel
import ru.ikrom.youtube_data.model.TrackModel
import javax.inject.Inject

class MediaRepositoryImpl @Inject constructor(
    private val dataSource: IMediaDataSource,
    private val localSource: ILocalDataSource
): IMediaRepository {
    override suspend fun getArtistData(id: String): ArtistPageModel {
        return dataSource.getArtistData(id).toArtistPageModel()
    }

    override suspend fun getTracksByQuery(query: String): List<TrackModel> {
        return dataSource.getTracksByQuery(query).mapNotNull { it.toTrackModel() }
    }

    override suspend fun getNewReleases(): List<AlbumModel> {
        return dataSource.getNewReleaseAlbums().map { it.toAlbumModel() }
    }

    override suspend fun getRadioTracks(id: String): List<TrackModel> {
        return dataSource.getRadioTracks(id).mapNotNull { it.toTrackModel() }
    }

    override suspend fun getAlbumTracks(albumId: String): List<TrackModel> {
        return dataSource.getAlbumTracks(albumId).mapNotNull { it.toTrackModel() }
    }

    override suspend fun getPlaylistTracks(playlistId: String): List<TrackModel> {
        return dataSource.getPlaylistTracks(playlistId).mapNotNull { it.toTrackModel() }
    }

    override suspend fun isFavorite(id: String): Boolean {
        return localSource.isLikedTrack(id)
    }

    override suspend fun getAlbumPage(id: String): AlbumPageModel {
        return dataSource.getAlbumPage(id).toModel()
    }

    override suspend fun saveTrack(id: String){
        localSource.saveTrack(getTracksByQuery(id).first().toEntity())
    }

    override suspend fun getLikedTracks(): List<TrackModel> {
        return localSource.getAllTracks().map { it.toModel() }
    }
}