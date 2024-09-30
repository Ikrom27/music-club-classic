package com.ikrom.music_club_classic.viewmodel


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ru.ikrom.ui.models.toThumbnailLargeItem
import ru.ikrom.ui.models.toThumbnailMediumItem
import com.ikrom.music_club_classic.extensions.models.toThumbnailRoundedItem
import ru.ikrom.ui.models.toThumbnailSmallItem
import ru.ikrom.ui.base_adapter.delegates.ArtistHeaderItem
import com.ikrom.music_club_classic.ui.items.ArtistPageItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.ikrom.youtube_data.IMediaRepository
import ru.ikrom.youtube_data.model.ArtistPageModel
import javax.inject.Inject

@HiltViewModel
class ArtistViewModel @Inject constructor(
    val repository: IMediaRepository
): ViewModel() {
    val artistItemLiveData = MutableLiveData<ArtistPageItem>()
    val artistModelLiveData = MutableLiveData<ArtistPageModel>()
    var artistId = ""
        set(value) {
            field = value
            updateArtistData(value)
        }

    fun updateArtistData(artistId: String){
        viewModelScope.launch {
            val artistPage = repository.getArtistData(artistId)
            artistModelLiveData.postValue(artistPage)
            artistItemLiveData.postValue(ArtistPageItem(
                header = ArtistHeaderItem(
                    title = artistPage.title,
                    subtitle = "",
                    thumbnail = artistPage.thumbnail
                ),
                tracks = artistPage.tracks.map { it.toThumbnailSmallItem() },
                albums = artistPage.albums.map { it.toThumbnailLargeItem() },
                singles = artistPage.singles.map { it.toThumbnailLargeItem() },
                relatedPlaylists = artistPage.relatedPlaylists.map { it.toThumbnailMediumItem() },
                similar = artistPage.similar.map { it.toThumbnailRoundedItem() }
            ))
        }
    }
}
