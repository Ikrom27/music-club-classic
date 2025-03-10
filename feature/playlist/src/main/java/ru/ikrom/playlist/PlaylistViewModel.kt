package ru.ikrom.playlist

import android.util.Log
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.ikrom.adapter_delegates.ext.toMediaItem
import ru.ikrom.adapter_delegates.ext.toMediaItems
import ru.ikrom.adapter_delegates.modelsExt.toThumbnailHeaderItem
import ru.ikrom.adapter_delegates.modelsExt.toThumbnailSmallItem
import ru.ikrom.base_adapter.ThumbnailItem
import ru.ikrom.base_fragment.DefaultStateViewModel
import ru.ikrom.player_handler.IPlayerHandler
import ru.ikrom.youtube_data.IMediaRepository
import ru.ikrom.youtube_data.model.PlaylistModel
import javax.inject.Inject

@HiltViewModel
class PlaylistViewModel @Inject constructor(
    private val playerHandler: IPlayerHandler,
    private val repository: IMediaRepository
) : DefaultStateViewModel<PlaylistPageContent>() {
    private var playlistModel: PlaylistModel? = null

    fun setupPlaylist(id: String){
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                repository.getPlaylistPage(id)
            }.onSuccess { page ->
                playlistModel = page.playlistInfo
                _state.postValue(PlaylistPageContent(
                    header = page.playlistInfo.toThumbnailHeaderItem(),
                    tracks = page.tracks.map { it.toThumbnailSmallItem() }
                ))
            }.onFailure {e ->
                Log.e(TAG, e.message.toString())
            }
        }
    }

    fun getPlaylistItems(): ThumbnailItem? {
        return playlistModel?.let { playlist ->
            object : ThumbnailItem {
                override val id: String
                    get() = playlist.id
                override val title: String
                    get() = playlist.title
                override val subtitle: String = "User playlist"
                override val thumbnail: String
                    get() = playlist.thumbnail
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

    companion object {
        val TAG = PlaylistViewModel::class.simpleName
    }
}
