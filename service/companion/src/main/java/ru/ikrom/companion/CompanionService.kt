package ru.ikrom.companion

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import ru.ikrom.player_handler.utils.IAudioDataListener
import java.util.concurrent.LinkedBlockingQueue


class CompanionService: IAudioDataListener {
    private val streamClient = AudioStreamClient()
    private val audioFramesQueue = LinkedBlockingQueue<ByteArray>()
    private var connectJob: Job? = null
    private var streamingJob: Job? = null
    private var isReady = false

    override fun connect(ip: String, port: Int) {
        isReady = true
        connectJob = CoroutineScope(Dispatchers.IO).launch {
            streamClient.connect(ip, port)
        }
        streamingJob = CoroutineScope(Dispatchers.IO).launch {
            connectJob?.join()
            while (isReady) {
                audioFramesQueue.take()?.let { audioFrame ->
                    streamClient.sendData(audioFrame)
                }
            }
        }
    }

    override fun release() {
        streamClient.disconnect()
        connectJob?.cancel()
        streamingJob?.cancel()
        isReady = false
    }

    override fun onAudioData(data: ByteArray) {
        if(!isReady) return
        audioFramesQueue.add(data)
    }
}