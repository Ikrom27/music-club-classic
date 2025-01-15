package ru.ikrom.player_repository

import androidx.media3.exoplayer.source.MediaSource

interface IPlayerRepository {
    fun getMediaSourceFactory(): MediaSource.Factory
}