package ru.ikrom.menu_track

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.ikrom.player.IPlayerHandler
import ru.ikrom.ui.base_adapter.delegates.MenuHeaderDelegateItem
import ru.ikrom.ui.models.toMenuHeaderItem
import ru.ikrom.youtube_data.IMediaRepository
import ru.ikrom.youtube_data.model.TrackModel
import ru.ikrom.youtube_data.model.getNames
import javax.inject.Inject

@HiltViewModel
class TrackMenuViewModel @Inject constructor(
    private val repository: IMediaRepository,
    private val playerHandler: IPlayerHandler
): ViewModel() {
    private var track: TrackModel? = null
    private val _uiContent = MutableLiveData<MenuHeaderDelegateItem>()
    val uiContent: LiveData<MenuHeaderDelegateItem> = _uiContent
    val shareLink: String
        get() = track?.shareLink ?: ""

    fun setTrack(id: String, title: String, subtitle: String, thumbnail: String){
        _uiContent.postValue(MenuHeaderDelegateItem(title, subtitle, thumbnail, false))
        updateTrackById(id)
    }

    private fun updateTrackById(id: String){
        viewModelScope.launch(Dispatchers.IO){
            runCatching {
                track = repository.getTracksByQuery(id).first()
                _uiContent.postValue(track!!.toMenuHeaderItem(repository.isFavorite(id)))
            }
        }
    }

    fun toggleFavorite(){
        viewModelScope.launch(Dispatchers.IO) {
            if(_uiContent.value?.isFavorite!!){
                track?.let { repository.unLikeTrack(it) }
            } else{
                track?.videoId?.let { repository.saveTrack(it) }
            }
            _uiContent.value?.apply {
                _uiContent.postValue(copy(isFavorite = !isFavorite))
            }
        }
    }

    fun addToQueue(){
        track?.let { playerHandler.addToQueue(it) }
    }

    fun download() {}
    fun getAlbumId(): String = track?.album?.id ?: ""
    fun getArtistId(): String = track?.album?.artists?.first()?.id ?: ""
}