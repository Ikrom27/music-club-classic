package com.ikrom.music_club_classic

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.Window
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.ikrom.music_club_classic.extensions.togglePlayPause
import com.ikrom.music_club_classic.playback.MusicPlayerService
import com.ikrom.music_club_classic.playback.PlayerConnection
import com.ikrom.music_club_classic.ui.components.MiniPlayerView
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var playerConnection: PlayerConnection

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val intent = Intent(this, MusicPlayerService::class.java)
        startForegroundService(intent)
        setStatusBarColor()
        setupMiniPlayer()
    }

    fun setupMiniPlayer(){
        val miniPlayerView = findViewById<MiniPlayerView>(R.id.mini_player)
        miniPlayerView.setOnButtonClickListener { playerConnection.player.togglePlayPause() }
        playerConnection.getCurrentMediaItem().observe(this) {
            if (it != null){
                miniPlayerView.show()
                miniPlayerView.title = it.mediaMetadata.title.toString()
                miniPlayerView.subTitle = it.mediaMetadata.artist.toString()
                Glide
                    .with(this)
                    .load(it.mediaMetadata.artworkUri?.toString())
                    .into(miniPlayerView.getThumbnailImageView())
            }
        }
        playerConnection.isPlaying.observe(this) {
            miniPlayerView.btnIcon = if (it) R.drawable.ic_pause else R.drawable.ic_play
        }
    }

    override fun onStart() {
        super.onStart()
        val navView = findViewById<BottomNavigationView>(R.id.bottom_navigation_bar)
        val navController = findNavController(R.id.nav_host_fragment)
        NavigationUI.setupWithNavController(navView, navController);
    }

    override fun onDestroy() {
        super.onDestroy()
        val intent = Intent(this, MusicPlayerService::class.java)
        stopService(intent)
    }

    private fun setStatusBarColor(){
        val window: Window = window
        window.statusBarColor = Color.TRANSPARENT
    }
}
