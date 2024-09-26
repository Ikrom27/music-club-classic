package com.ikrom.music_club_classic.domain

import ru.ikrom.youtube_data.IMediaRepository
import ru.ikrom.youtube_data.model.TrackModel
import javax.inject.Inject

class RecommendedUseCase @Inject constructor(
    private val mediaRepository: IMediaRepository
) {
    var isAuthorized = false

    suspend fun getRecommendedTracks(): List<TrackModel> {
        if (isAuthorized){
            val trackId = mediaRepository.getPlaylistTracks(LIKED_TRACKS_ID).shuffled()[0].videoId
            return mediaRepository.getRadioTracks(trackId).shuffled()
        }
        return emptyList()
    }

    companion object {
        private const val LIKED_TRACKS_ID = "LM"
    }
}