package ru.ikrom.album

import android.util.Log
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.ikrom.adapter_delegates.base.ThumbnailItem
import ru.ikrom.adapter_delegates.base.toMediaItem
import ru.ikrom.adapter_delegates.base.toMediaItems
import ru.ikrom.adapter_delegates.modelsExt.toThumbnailHeaderItem
import ru.ikrom.adapter_delegates.modelsExt.toThumbnailSmallItem
import ru.ikrom.base_fragment.DefaultStateViewModel
import ru.ikrom.player_handler.IPlayerHandler
import ru.ikrom.youtube_data.IMediaRepository
import ru.ikrom.youtube_data.model.AlbumModel
import ru.ikrom.youtube_data.model.getNames
import javax.inject.Inject

@HiltViewModel
class AlbumViewModel @Inject constructor(
    private val playerHandler: IPlayerHandler,
    private val repository: IMediaRepository
) : DefaultStateViewModel<AlbumPageContent>() {
    private var albumModel: AlbumModel? = null

    fun updateAlbum(id: String){
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                repository.getAlbumPage(id)
            }.onSuccess { page ->
                albumModel = page.albumInfo
                _state.postValue(AlbumPageContent(
                    header = page.albumInfo.toThumbnailHeaderItem(),
                    tracks = page.tracks.map { it.toThumbnailSmallItem() }
                ))
            }.onFailure {e ->
                Log.e(TAG, e.message.toString())
            }
        }
    }

    fun getAlbumItem(): ThumbnailItem? {
        return albumModel?.let { album ->
            object : ThumbnailItem {
                override val id: String
                    get() = album.id
                override val title: String
                    get() = album.title
                override val subtitle: String
                    get() = album.artists.getNames()
                override val thumbnail: String
                    get() = album.thumbnail
            }
        }
    }

    fun playAllTracks(){
        _state.value?.let {
            playerHandler.playNow(it.tracks.toMediaItems())
        }
    }

    fun playShuffled(){
        _state.value?.let {
            playerHandler.playShuffle(it.tracks.toMediaItems())
        }
    }

    fun playTrack(track: ThumbnailItem){
        playerHandler.playNow(track.toMediaItem())
    }

    fun getArtistId(): String? {
        return albumModel?.artists?.first()?.id
    }

    companion object {
        val TAG = AlbumViewModel::class.simpleName
    }
}