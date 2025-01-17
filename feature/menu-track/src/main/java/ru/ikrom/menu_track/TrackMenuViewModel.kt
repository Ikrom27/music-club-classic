package ru.ikrom.menu_track

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.ikrom.adapter_delegates.delegates.MenuHeaderDelegateItem
import ru.ikrom.adapter_delegates.ext.toMediaItem
import ru.ikrom.adapter_delegates.modelsExt.toMenuHeaderItem
import ru.ikrom.fragment_bottom_menu.BaseBottomMenuViewModel
import ru.ikrom.player_handler.IPlayerHandler
import ru.ikrom.youtube_data.IMediaRepository
import ru.ikrom.youtube_data.model.TrackModel
import javax.inject.Inject

@HiltViewModel
class TrackMenuViewModel @Inject constructor(
    private val repository: IMediaRepository,
    private val playerHandler: IPlayerHandler
): BaseBottomMenuViewModel<MenuHeaderDelegateItem>() {
    private var track: TrackModel? = null

    val shareLink: String
        get() = track?.shareLink ?: ""

    private fun updateTrackById(id: String){
        viewModelScope.launch(Dispatchers.IO){
            runCatching {
                track = repository.getTracksByQuery(id).first()
                _headerState.postValue(track!!.toMenuHeaderItem(repository.isFavorite(id)))
            }
        }
    }

    fun toggleFavorite(){
        viewModelScope.launch(Dispatchers.IO) {
            if(_headerState.value?.isFavorite!!){
                track?.let { repository.unLikeTrack(it) }
            } else{
                track?.videoId?.let { repository.saveTrack(it) }
            }
            _headerState.value?.apply {
                _headerState.postValue(copy(isFavorite = !isFavorite))
            }
        }
    }

    fun addToQueue(){
        _headerState.value?.let { playerHandler.addToQueue(it.toMediaItem()) }
    }

    fun download() {}
    fun getAlbumId(): String = track?.album?.id ?: ""
    fun getArtistId(): String = track?.album?.artists?.first()?.id ?: ""

    override fun setupHeader(id: String, title: String, subtitle: String, thumbnail: String) {
        _headerState.postValue(MenuHeaderDelegateItem(id, title, subtitle, thumbnail, false))
        updateTrackById(id)
    }
}