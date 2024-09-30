package com.ikrom.music_club_classic.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ru.ikrom.ui.models.toCardItem
import ru.ikrom.ui.base_adapter.delegates.CardItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.ikrom.youtube_data.IMediaRepository
import javax.inject.Inject

@HiltViewModel
class ExploreViewModel @Inject constructor(
    val repository: IMediaRepository,
//    val navigator: Navigator,
) : ViewModel() {
    val newReleasesList = MutableLiveData<List<CardItem>>()
    init {
        update()
    }
    fun update(){
        viewModelScope.launch {
            newReleasesList.postValue(repository.getNewReleases().map { it.toCardItem() })
        }
    }

    fun toSearchScreen(){
//        navigator.toSearchScreen()
    }

    interface Navigator {
        fun toSearchScreen()
    }
}