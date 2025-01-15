package ru.ikrom.menu_artist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.ikrom.adapter_delegates.delegates.MenuHeaderDelegateItem
import ru.ikrom.adapter_delegates.modelsExt.toMenuHeaderItem
import ru.ikrom.youtube_data.IMediaRepository
import ru.ikrom.youtube_data.model.ArtistModel
import javax.inject.Inject

@HiltViewModel
class ArtistMenuViewModel @Inject constructor(
    private val repository: IMediaRepository
): ViewModel() {
    private var artist: ArtistModel? = null
    private val _uiContent = MutableLiveData<MenuHeaderDelegateItem>()
    val uiContent: LiveData<MenuHeaderDelegateItem> = _uiContent
    val shareLink: String
        get() = artist?.shareLink ?: ""

    fun setArtist(id: String, title: String, subtitle: String, thumbnail: String){
        _uiContent.postValue(MenuHeaderDelegateItem(id, title, subtitle, thumbnail, false))
        updateContent(id)
    }

    private fun updateContent(id: String){
        viewModelScope.launch(Dispatchers.IO){
            runCatching {
                artist = repository.getArtistData(id).toArtistModel()
                _uiContent.postValue(artist!!.toMenuHeaderItem(repository.isFavoriteArtist(id)))
            }
        }
    }

    fun toggleFavorite(){
        viewModelScope.launch(Dispatchers.IO) {
            if(_uiContent.value?.isFavorite!!){
                artist?.let { repository.unLikeArtist(it) }
            } else{
                artist?.let { repository.likeArtist(it) }
            }
            _uiContent.value?.apply {
                _uiContent.postValue(copy(isFavorite = !isFavorite))
            }
            artist?.id?.let { updateContent(it) }
        }
    }

    fun getArtistId(): String = artist?.id ?: ""
}