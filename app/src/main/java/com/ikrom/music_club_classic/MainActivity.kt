package com.ikrom.music_club_classic

import android.R.color
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.Window
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.ikrom.music_club_classic.playback.MusicPlayerService
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val intent = Intent(this, MusicPlayerService::class.java)
        startForegroundService(intent)
        setStatusBarColor()
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
