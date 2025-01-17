package ru.ikrom.menu_artist

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.ikrom.adapter_delegates.delegates.MenuHeaderDelegateItem
import ru.ikrom.adapter_delegates.modelsExt.toMenuHeaderItem
import ru.ikrom.fragment_bottom_menu.BaseBottomMenuViewModel
import ru.ikrom.youtube_data.IMediaRepository
import ru.ikrom.youtube_data.model.ArtistModel
import javax.inject.Inject

@HiltViewModel
class ArtistMenuViewModel @Inject constructor(
    private val repository: IMediaRepository
): BaseBottomMenuViewModel<MenuHeaderDelegateItem>()  {
    private var artist: ArtistModel? = null

    val shareLink: String
        get() = artist?.shareLink ?: ""

    override fun setupHeader(id: String, title: String, subtitle: String, thumbnail: String){
        _headerState.postValue(MenuHeaderDelegateItem(id, title, subtitle, thumbnail, false))
        updateContent(id)
    }

    private fun updateContent(id: String){
        viewModelScope.launch(Dispatchers.IO){
            runCatching {
                artist = repository.getArtistData(id).toArtistModel()
                _headerState.postValue(artist!!.toMenuHeaderItem(repository.isFavoriteArtist(id)))
            }
        }
    }

    fun toggleFavorite(){
        viewModelScope.launch(Dispatchers.IO) {
            if(_headerState.value?.isFavorite!!){
                artist?.let { repository.unLikeArtist(it) }
            } else{
                artist?.let { repository.likeArtist(it) }
            }
            _headerState.value?.apply {
                _headerState.postValue(copy(isFavorite = !isFavorite))
            }
            artist?.id?.let { updateContent(it) }
        }
    }

    fun getArtistId(): String = artist?.id ?: ""
}