package ru.ikrom.player_handler.utils

interface IAudioDataListener {
    fun connect(ip: String, port: Int)
    fun onAudioData(data: ByteArray)
    fun release()
}