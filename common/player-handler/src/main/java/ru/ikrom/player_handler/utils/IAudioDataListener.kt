package ru.ikrom.player_handler.utils

interface IAudioDataListener {
    fun prepare()
    fun onAudioData(data: ByteArray)
}