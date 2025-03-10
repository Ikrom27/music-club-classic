package ru.ikrom.youtube_data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.ikrom.database.ILocalDataSource
import ru.ikrom.youtube_data.extensions.toAlbumModel
import ru.ikrom.youtube_data.extensions.toAlbumModels
import ru.ikrom.youtube_data.extensions.toArtistPageModel
import ru.ikrom.youtube_data.extensions.toEntity
import ru.ikrom.youtube_data.extensions.toModel
import ru.ikrom.youtube_data.extensions.toTrackModels
import ru.ikrom.youtube_data.extensions.toArtistModels
import ru.ikrom.youtube_data.extensions.toTrackModel
import ru.ikrom.youtube_data.model.AlbumModel
import ru.ikrom.youtube_data.model.AlbumPageModel
import ru.ikrom.youtube_data.model.ArtistModel
import ru.ikrom.youtube_data.model.ArtistPageModel
import ru.ikrom.youtube_data.model.PlaylistModel
import ru.ikrom.youtube_data.model.PlaylistPageModel
import ru.ikrom.youtube_data.model.TrackModel
import javax.inject.Inject

internal class MediaRepositoryImpl @Inject constructor(
    private val dataSource: IMediaDataSource,
    private val localSource: ILocalDataSource
) : IMediaRepository {

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

    override suspend fun isFavoriteArtist(id: String): Boolean {
        return localSource.isLikedArtist(id)
    }

    override suspend fun isFavoriteAlbum(albumId: String): Boolean {
        return localSource.isLikedAlbum(albumId)
    }

    override suspend fun getAlbumPage(id: String): AlbumPageModel {
        return dataSource.getAlbumPage(id).toModel()
    }

    override suspend fun getPlaylistPage(id: String): PlaylistPageModel {
        return PlaylistPageModel(
            playlistInfo = localSource.getPlaylistById(id).toModel(),
            tracks = localSource.getPlaylistTracks(id).toTrackModels()
        )
    }

    override suspend fun getLikedTracks(): Flow<List<TrackModel>> {
        return localSource.getLikedTracks().map { it.toTrackModels() }
    }

    override suspend fun getLikedArtists(): Flow<List<ArtistModel>> {
        return localSource.getLikedArtists().map { it.toArtistModels() }
    }

    override suspend fun saveTrack(id: String) {
        localSource.saveTrack(getTracksByQuery(id).first().toEntity())
    }

    override suspend fun unLikeTrack(track: TrackModel) {
        localSource.removeTrack(track.toEntity())
    }

    override suspend fun likeArtist(artistModel: ArtistModel) {
        artistModel.toEntity()?.let { localSource.saveArtist(it) }
    }

    override suspend fun unLikeArtist(artistModel: ArtistModel) {
        artistModel.toEntity()?.let { localSource.removeArtist(it) }
    }

    override suspend fun likeAlbum(albumModel: AlbumModel) {
        localSource.saveAlbum(albumModel.toEntity())
    }

    override suspend fun unLikeAlbum(albumModel: AlbumModel) {
        localSource.removeAlbum(albumModel.toEntity())
    }

    override suspend fun getLikedAlbums(): Flow<List<AlbumModel>> {
        return localSource.getLikedAlbums().map {it.toAlbumModels()}
    }

    override suspend fun getSavedPlaylists(): Flow<List<PlaylistModel>> {
        return localSource.getSavedPlaylists().map { it.map { it.toModel() } }
    }

    override suspend fun savePlaylist(playlistModel: PlaylistModel){
        localSource.savePlaylist(playlistModel.toEntity())
    }
}
