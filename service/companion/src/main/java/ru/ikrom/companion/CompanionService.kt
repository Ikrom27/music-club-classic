package ru.ikrom.companion

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import ru.ikrom.player_handler.utils.IAudioDataListener
import java.util.concurrent.LinkedBlockingQueue


class CompanionService: IAudioDataListener {
    private val streamClient = AudioStreamClient("192.168.1.148", 5000)
    private val audioFramesQueue = LinkedBlockingQueue<ByteArray>()
    private var connectJob: Job? = null
    private var streamingJob: Job? = null
    private var isReady = false
    private var counter = 0

    fun connect() {
        connectJob = CoroutineScope(Dispatchers.IO).launch {
            streamClient.connect()
        }
        streamingJob = CoroutineScope(Dispatchers.IO).launch {
            connectJob?.join()
            while (isReady) {
                audioFramesQueue.take()?.let { audioFrame ->
//                    if(counter < 100 && audioFrame.isNotEmpty()){
//                        println("#${counter++} - [${audioFrame.joinToString(" ")}]")
//                    }
                    streamClient.sendData(audioFrame)
                }
            }
        }
    }

    fun release() {
        streamClient.disconnect()
        connectJob?.cancel()
        streamingJob?.cancel()
        isReady = false
    }

    override fun prepare() {
        connect()
        isReady = true
    }

    override fun onAudioData(data: ByteArray) {
        if(!isReady) return
        audioFramesQueue.add(data)
    }
}