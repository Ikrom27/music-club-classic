package com.ikrom.music_club_classic.playback


import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.OptIn
import androidx.annotation.RequiresApi
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@OptIn(UnstableApi::class)
@AndroidEntryPoint
class MusicPlayerService: MediaSessionService() {
    private val TAG = "MusicPlayerService"

    @Inject
    lateinit var player: ExoPlayer

    @Inject
    lateinit var notificationManager: MusicNotificationManager
    private lateinit var mediaSession: MediaSession

    init {
        Log.d(TAG, "Service init")
    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession? =
        mediaSession

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "Service start")
        mediaSession = MediaSession.Builder(this, player).build()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        Log.d(TAG, "onStartCommand")
        notificationManager.start(this, mediaSession)
        return START_STICKY_COMPATIBILITY
    }

    override fun onDestroy() {
        mediaSession.run {
            release()
            destroyPlayer()
        }
        notificationManager.stopForeground(this)
        super.onDestroy()
    }

    private fun destroyPlayer() {
        player.seekTo(0)
        player.playWhenReady = false
        player.stop()
        player.release()
    }
}