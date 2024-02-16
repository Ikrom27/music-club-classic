package com.ikrom.music_club_classic

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.Window
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.ikrom.music_club_classic.playback.MusicPlayerService
import com.ikrom.music_club_classic.playback.PlayerHandler
import com.ikrom.music_club_classic.ui.MainFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var playerHandler: PlayerHandler

    @SuppressLint("CommitTransaction")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, MainFragment())
                .commit()
        }
        setStatusBarColor()
        setupPlayerService()
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

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setupPlayerService(){
        val intent = Intent(this, MusicPlayerService::class.java)
        startForegroundService(intent)
    }
}
