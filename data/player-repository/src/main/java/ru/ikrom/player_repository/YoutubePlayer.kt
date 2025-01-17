package ru.ikrom.player_repository

import android.net.Uri
import androidx.core.net.toUri
import com.zionhuang.innertube.YouTube
import com.zionhuang.innertube.models.response.PlayerResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

internal object YoutubePlayer {
    fun getProxy() = YouTube.proxy
    fun getUri(mediaId: String): Uri {
        return getFormat(mediaId)?.url?.toUri() ?: "".toUri()
    }
    private fun getFormat(mediaId: String): PlayerResponse.StreamingData.Format? {
        return getPlayerResponse(mediaId).getOrNull()?.streamingData?.adaptiveFormats
            ?.filter { it.isAudio }
            ?.maxByOrNull {
                it.bitrate * 1 + (if (it.mimeType.startsWith("audio/webm")) 10240 else 0)
            }
    }
    private fun getPlayerResponse(mediaId: String) = runBlocking(Dispatchers.IO) {
        YouTube.player(mediaId)
    }
}