package com.ikrom.music_club_classic

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onStart() {
        super.onStart()
        val navView = findViewById<BottomNavigationView>(R.id.bottom_navigation_bar)
        val navController = findNavController(R.id.nav_host_fragment)
        NavigationUI.setupWithNavController(navView, navController);
    }
}
