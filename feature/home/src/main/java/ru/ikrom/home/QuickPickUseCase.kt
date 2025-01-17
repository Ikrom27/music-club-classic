package ru.ikrom.home

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import ru.ikrom.youtube_data.IMediaRepository
import ru.ikrom.youtube_data.model.TrackModel
import javax.inject.Inject

class QuickPickUseCase @Inject constructor(private val repository: IMediaRepository) {
    suspend fun getQuickPickTracks(): List<TrackModel> {
        val likedTrack = repository.getLikedTracks().firstOrNull()?.randomOrNull()
        return repository.getRadioTracks(likedTrack?.videoId ?: EXAMPLE_TRACK_ID)
    }

    companion object {
        const val EXAMPLE_TRACK_ID = "Kx7B-XvmFtE"
    }
}