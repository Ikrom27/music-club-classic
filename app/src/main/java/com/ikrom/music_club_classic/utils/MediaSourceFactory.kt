package com.ikrom.music_club_classic.utils

import android.content.Context
import androidx.annotation.OptIn
import androidx.core.net.toUri
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DataSource
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.datasource.ResolvingDataSource
import androidx.media3.datasource.cache.CacheDataSource
import androidx.media3.datasource.cache.SimpleCache
import androidx.media3.datasource.okhttp.OkHttpDataSource
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
import androidx.media3.extractor.ExtractorsFactory
import androidx.media3.extractor.mkv.MatroskaExtractor
import androidx.media3.extractor.mp4.FragmentedMp4Extractor
import com.ikrom.music_club_classic.di.DownloadCacheScope
import com.ikrom.music_club_classic.di.PlayerCacheScope
import okhttp3.OkHttpClient
import ru.ikrom.youtube_data.YoutubePlayer
import javax.inject.Inject

@OptIn(UnstableApi::class)
class MediaSourceFactory @Inject constructor(
    @DownloadCacheScope
    val downloadCache: SimpleCache,
    @PlayerCacheScope
    var playerCache: SimpleCache,
) {
    private val CHUNK_LENGTH = 512 * 1024L


    fun createMediaSourceFactory(context: Context): DefaultMediaSourceFactory {
        return DefaultMediaSourceFactory(createDataSourceFactory(context), createExtractorsFactory())
    }

    private fun createExtractorsFactory() = ExtractorsFactory {
        arrayOf(MatroskaExtractor(), FragmentedMp4Extractor())
    }

    private fun createOkHttpDataSourceFactory() =
        OkHttpDataSource.Factory(
            OkHttpClient.Builder()
                .proxy(YoutubePlayer.getProxy())
                .build()
        )

    private fun createCacheDataSource(context: Context): CacheDataSource.Factory {
        return CacheDataSource.Factory()
            .setCache(downloadCache)
            .setUpstreamDataSourceFactory(
                CacheDataSource.Factory()
                    .setCache(playerCache)
                    .setUpstreamDataSourceFactory(DefaultDataSource.Factory(context, createOkHttpDataSourceFactory()))
            )
            .setCacheWriteDataSinkFactory(null)
            .setFlags(CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR)
    }


    private fun createDataSourceFactory(context: Context): DataSource.Factory {
        val songUrlCache = HashMap<String, Pair<String, Long>>()
        return ResolvingDataSource.Factory(createCacheDataSource(context)) { dataSpec ->
            val mediaId = dataSpec.key ?: error("No media id")
            val length = if (dataSpec.length >= 0) dataSpec.length else 1

            if (downloadCache.isCached(mediaId, dataSpec.position, length) || playerCache.isCached(mediaId, dataSpec.position, length)) {
                return@Factory dataSpec
            }

            songUrlCache[mediaId]?.takeIf { it.second < System.currentTimeMillis() }?.let {
                return@Factory dataSpec.withUri(it.first.toUri())
            }

            dataSpec.withUri(YoutubePlayer.getUri(mediaId)).subrange(dataSpec.uriPositionOffset, CHUNK_LENGTH)
        }
    }
}