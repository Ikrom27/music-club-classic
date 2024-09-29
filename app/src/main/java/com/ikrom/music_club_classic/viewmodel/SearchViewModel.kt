package com.ikrom.music_club_classic.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ikrom.music_club_classic.extensions.models.toThumbnailSmallItem
import com.ikrom.music_club_classic.ui.adapters.delegates.ThumbnailSmallItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.ikrom.player.PlayerHandlerImpl
import ru.ikrom.youtube_data.IMediaRepository
import ru.ikrom.youtube_data.model.TrackModel
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    val playerHandler: PlayerHandlerImpl,
    val repository: IMediaRepository
): ViewModel() {
    val globalResultList = MutableLiveData<List<ThumbnailSmallItem>>(emptyList())
    val localResultList = MutableLiveData<List<ThumbnailSmallItem>>(emptyList())
    val serverStatus = MutableLiveData<Int>()
    private val localModelResults = ArrayList<TrackModel>()
    private val globalModelResults = ArrayList<TrackModel>()
    private var lastQuery = ""

    fun updateSearchList(query: String = lastQuery) {
        if (query.isEmpty()){
            return
        }
        lastQuery = query
        updateLocalResult(query)
        updateGlobalResult(query)
    }

    fun updateLocalResult(query: String) {
        if (localModelResults.isNotEmpty()){
            localResultList.value = localModelResults.filter {
                it.title
                    .lowercase()
                    .trim()
                    .contains(query
                        .lowercase()
                        .trim()
                    )
            }.map {
                it.toThumbnailSmallItem()
            }
        }
    }

    fun updateGlobalResult(query: String){
        viewModelScope.launch {
            globalResultList.postValue(
                repository.getTracksByQuery(query).map { it.toThumbnailSmallItem() })
        }
    }

    fun getTrackById(id: String): TrackModel{
        return (localModelResults + globalModelResults).first { it.videoId == id }
    }

    fun playTrackById(id: String) {
        playerHandler.playNow(getTrackById(id))
    }

//    fun addContentList(tracks: List<TrackModel>){
//        contentList.clear()
//        contentList.addAll(tracks)
//    }
}