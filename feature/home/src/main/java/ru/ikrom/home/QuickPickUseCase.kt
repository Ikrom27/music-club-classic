package ru.ikrom.home

import kotlinx.coroutines.flow.first
import ru.ikrom.youtube_data.IMediaRepository
import ru.ikrom.youtube_data.model.TrackModel
import javax.inject.Inject

class QuickPickUseCase @Inject constructor(private val repository: IMediaRepository) {
    suspend fun getQuickPickTracks(): List<TrackModel> {
        val likedTrack = repository.getLikedTracks().first().random()
        return repository.getRadioTracks(likedTrack.videoId)
    }
}