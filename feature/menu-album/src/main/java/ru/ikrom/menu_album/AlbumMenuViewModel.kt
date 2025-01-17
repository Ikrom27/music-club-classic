package ru.ikrom.menu_album

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.ikrom.adapter_delegates.delegates.MenuHeaderDelegateItem
import ru.ikrom.adapter_delegates.modelsExt.toMenuHeaderItem
import ru.ikrom.fragment_bottom_menu.BaseBottomMenuViewModel
import ru.ikrom.youtube_data.IMediaRepository
import ru.ikrom.youtube_data.model.AlbumModel
import javax.inject.Inject

@HiltViewModel
class AlbumMenuViewModel @Inject constructor(
    private val repository: IMediaRepository
): BaseBottomMenuViewModel<MenuHeaderDelegateItem>()  {
    private var album: AlbumModel? = null

    val shareLink: String
        get() = album?.shareLink ?: ""

    override fun setupHeader(id: String, title: String, subtitle: String, thumbnail: String){
        _headerState.postValue(MenuHeaderDelegateItem(id, title, subtitle, thumbnail, false))
        updateContent(id)
    }

    private fun updateContent(id: String){
        viewModelScope.launch(Dispatchers.IO){
            runCatching {
                album = repository.getAlbumPage(id).albumInfo
                _headerState.postValue(album!!.toMenuHeaderItem(repository.isFavoriteAlbum(id)))
            }
        }
    }

    fun toggleFavorite(){
        viewModelScope.launch(Dispatchers.IO) {
            if(_headerState.value?.isFavorite!!){
                album?.let { repository.unLikeAlbum(it) }
            } else{
                album?.let { repository.likeAlbum(it) }
            }
            _headerState.value?.apply {
                _headerState.postValue(copy(isFavorite = !isFavorite))
            }
            album?.id?.let { updateContent(it) }
        }
    }

    fun getAlbumId(): String = album?.id ?: ""
    fun getArtistId(): String = album?.artists?.first()?.id ?: ""
}