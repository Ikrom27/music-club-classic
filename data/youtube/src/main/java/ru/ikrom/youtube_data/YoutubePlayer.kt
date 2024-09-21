package ru.ikrom.youtube_data

import android.net.Uri
import androidx.core.net.toUri
import com.ikrom.innertube.YouTube
import com.ikrom.innertube.models.response.PlayerResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

object YoutubePlayer {
    fun getProxy() = YouTube.proxy
    fun getUri(mediaId: String): Uri {
        return getFormat(mediaId)!!.url!!.toUri()
    }
    private fun getFormat(mediaId: String): PlayerResponse. StreamingData. Format? {
        return getPlayerResponse(mediaId).getOrNull()?.streamingData?.adaptiveFormats
            ?.filter { it.isAudio }
            ?.maxByOrNull {
                it.bitrate * 1 + (if (it.mimeType.startsWith("audio/webm")) 10240 else 0) // prefer opus stream
            }
    }
    private fun getPlayerResponse(mediaId: String) = runBlocking(Dispatchers.IO) {
        YouTube.player(mediaId)
    }
}