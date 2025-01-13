package ru.ikrom.menu_track

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.ikrom.player.IPlayerHandler
import ru.ikrom.youtube_data.IMediaRepository
import ru.ikrom.youtube_data.model.TrackModel
import javax.inject.Inject

@HiltViewModel
class TrackMenuViewModel @Inject constructor(
    private val repository: IMediaRepository,
    private val playerHandler: IPlayerHandler
): ViewModel() {
    private var track: TrackModel? = null
    val shareLink: String
        get() = track?.shareLink ?: ""

    fun setTrackById(id: String){
        viewModelScope.launch(Dispatchers.IO){
            runCatching {
                track = repository.getTracksByQuery(id).first()
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