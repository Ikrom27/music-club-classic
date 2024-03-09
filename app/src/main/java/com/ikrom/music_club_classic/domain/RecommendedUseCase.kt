package com.ikrom.music_club_classic.domain

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asFlow
import com.ikrom.innertube.models.WatchEndpoint
import com.ikrom.music_club_classic.data.model.Track
import com.ikrom.music_club_classic.data.repository.AccountRepository
import com.ikrom.music_club_classic.data.repository.MediaRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class RecommendedUseCase @Inject constructor(
    private val mediaRepository: MediaRepository,
    accountRepository: AccountRepository
) {
    var isAuthorized = false
    init {
        isAuthorized = accountRepository.isAuthorised()
    }

    fun getRecommendedTracks(): LiveData<List<Track>> {
        val result = MutableLiveData<List<Track>>()
        if (isAuthorized){
            CoroutineScope(Dispatchers.IO).launch {
                mediaRepository.getPlaylistTracks(LIKED_TRACKS_ID).asFlow().collect {playlists ->
                    if (playlists.isNotEmpty()){
                        val trackId = playlists.shuffled()[0].videoId
                        Log.d("RecommendedUsaCase", "find track ${trackId}")
                        mediaRepository.getRadioTracks(WatchEndpoint(trackId)).asFlow().collect {tracks ->
                            if (tracks.isNotEmpty()){
                                result.postValue(tracks.shuffled())
                            }
                        }
                    }
                }
            }
        }
        return result
    }

    companion object {
        private const val LIKED_TRACKS_ID = "LM"
    }
}