package ru.ikrom.player_handler.utils

import android.content.Context
import androidx.annotation.OptIn
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.DefaultRenderersFactory
import androidx.media3.exoplayer.audio.AudioSink
import java.nio.ByteBuffer


@OptIn(UnstableApi::class)
class DataListenerRenderFactory
    (
    context: Context,
    private val audioDataListener: IAudioDataListener
) : DefaultRenderersFactory(context) {

    override fun buildAudioSink(
        context: Context,
        enableFloatOutput: Boolean,
        enableAudioTrackPlaybackParams: Boolean
    ): AudioSink {
        val defaultAudioSink = super.buildAudioSink(context, enableFloatOutput, enableAudioTrackPlaybackParams)
        return DataListenerAudioSink(defaultAudioSink!!, audioDataListener)
    }
}

@OptIn(UnstableApi::class)
class DataListenerAudioSink(
    private val wrappedSink: AudioSink,
    private val audioDataListener: IAudioDataListener
) : AudioSink by wrappedSink {
    private var prevBuffer: ByteArray? = null
    private var counter = 0

    override fun handleBuffer(
        buffer: ByteBuffer,
        presentationTimeUs: Long,
        encodedAccessUnitCount: Int
    ): Boolean {
        val copy = buffer.duplicate()
        val bytes = ByteArray(copy.remaining())
        copy[bytes]
        if(!bytes.contentEquals(prevBuffer)) {
            prevBuffer = bytes
            if(counter < 100 && bytes.isNotEmpty()){
                println("#${counter++} - [${bytes.joinToString(" ")}]")
            }
            audioDataListener.onAudioData(bytes)
        }
        return wrappedSink.handleBuffer(buffer, presentationTimeUs, encodedAccessUnitCount)
    }
}