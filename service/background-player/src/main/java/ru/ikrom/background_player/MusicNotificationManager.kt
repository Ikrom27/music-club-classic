package ru.ikrom.background_player

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.OptIn
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaController
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import androidx.media3.ui.PlayerNotificationManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.google.common.util.concurrent.ListenableFuture
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@UnstableApi
class MusicNotificationManager @Inject constructor (
    @ApplicationContext private val context: Context,
    private val player: ExoPlayer
) {
    private lateinit var playerNotificationManager: PlayerNotificationManager
    @UnstableApi
    private var notificationListener: PlayerNotificationManager.NotificationListener =
        object : PlayerNotificationManager.NotificationListener {}
    private val serviceJob = SupervisorJob()
    private val serviceScope = CoroutineScope(Dispatchers.Main + serviceJob)

    companion object {
        private const val CHANNEL_ID = "media_player_channel"
        private const val CHANNEL_NAME = "Media Player Channel"
        private const val CHANNEL_DESCRIPTION = "Channel for Media Player"
        private const val NOTIFICATION_ID = 1
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun start(
        mediaSessionService: MediaSessionService,
        mediaSession: MediaSession
    ){
        Log.d("Notification", "to start")
        initPlayerNotificationManager(mediaSession)
        startForeground(mediaSessionService)
    }

    @OptIn(UnstableApi::class)
    private fun initPlayerNotificationManager(mediaSession: MediaSession){
        val mediaController = MediaController.Builder(context, mediaSession.token).buildAsync()
        playerNotificationManager = PlayerNotificationManager.Builder(context, NOTIFICATION_ID, CHANNEL_ID)
            .setNotificationListener(notificationListener)
            .setMediaDescriptionAdapter(DescriptionAdapter(mediaController))
            .build()
            .also {
                it.setMediaSessionToken(mediaSession.platformToken)
                it.setPriority(NotificationCompat.PRIORITY_LOW)
                it.setUsePreviousActionInCompactView(true)
                it.setUseFastForwardAction(false)
                it.setUseRewindAction(false)
                it.setUseNextActionInCompactView(true)
                it.setPlayer(player)
            }
    }

    fun stopForeground(mediaSessionService: MediaSessionService){
        mediaSessionService.stopForeground(Service.STOP_FOREGROUND_REMOVE)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun startForeground(mediaSessionService: MediaSessionService){
        createNotificationChannel()
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setCategory(Notification.CATEGORY_SERVICE)
            .build()
        mediaSessionService.startForeground(NOTIFICATION_ID, notification)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            CHANNEL_ID,
            CHANNEL_NAME,
            NotificationManager.IMPORTANCE_LOW
        )
        channel.description = CHANNEL_DESCRIPTION

        val notificationManager = context.getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(channel)
    }

    @UnstableApi private inner class DescriptionAdapter(private val controller: ListenableFuture<MediaController>) :
        PlayerNotificationManager.MediaDescriptionAdapter {

        var currentIconUri: Uri? = null
        var currentBitmap: Bitmap? = null

        override fun createCurrentContentIntent(player: Player): PendingIntent? =
            controller.get().sessionActivity

        override fun getCurrentContentText(player: Player) =
            ""

        override fun getCurrentContentTitle(player: Player) =
            controller.get().mediaMetadata.title.toString()

        override fun getCurrentLargeIcon(
            player: Player,
            callback: PlayerNotificationManager.BitmapCallback
        ): Bitmap? {
            val iconUri = controller.get().mediaMetadata.artworkUri
            return if (currentIconUri != iconUri || currentBitmap == null) {
                currentIconUri = iconUri
                serviceScope.launch {
                    currentBitmap = iconUri?.let {
                        resolveUriAsBitmap(it)
                    }
                    currentBitmap?.let { callback.onBitmap(it) }
                }
                null
            } else {
                currentBitmap
            }
        }

        private suspend fun resolveUriAsBitmap(uri: Uri): Bitmap? {
            return withContext(Dispatchers.IO) {
                // Block on downloading artwork.
                Glide.with(context).applyDefaultRequestOptions(glideOptions)
                    .asBitmap()
                    .load(uri)
                    .submit(NOTIFICATION_LARGE_ICON_SIZE, NOTIFICATION_LARGE_ICON_SIZE)
                    .get()
            }
        }

        val NOTIFICATION_LARGE_ICON_SIZE = 144

        private val glideOptions = RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.DATA)
    }
}
