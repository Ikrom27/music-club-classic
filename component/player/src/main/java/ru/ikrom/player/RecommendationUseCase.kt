package ru.ikrom.player

import ru.ikrom.youtube_data.IMediaRepository
import ru.ikrom.youtube_data.model.TrackModel
import javax.inject.Inject

class RecommendationUseCase @Inject constructor(
    private val repository: IMediaRepository
) {
    suspend fun generateRecommendedTracks(seed: String = ""): List<TrackModel> {
        val likedTrack = repository.getLikedTracks().random()
        return repository.getRadioTracks(seed)
    }
}