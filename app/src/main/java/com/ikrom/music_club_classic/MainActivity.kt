package com.ikrom.music_club_classic

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.ikrom.music_club_classic.ui.MainFragment
import com.ikrom.music_club_classic.viewmodel.SettingsViewModel
import dagger.hilt.android.AndroidEntryPoint
import ru.ikrom.player.MusicPlayerService


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val settingsViewModel: SettingsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setThemeMode()
        setFragmentContainer(savedInstanceState)
        setupPlayerService()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (!isChangingConfigurations) {
            destroyService()
        }
    }

    override fun onResume() {
        super.onResume()
        disableInputAdjustment()
    }

    private fun setFragmentContainer(savedInstanceState: Bundle?){
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, MainFragment())
                .commit()
        }
    }
    private fun setThemeMode(){
        settingsViewModel.themeState.observe(this) {
            AppCompatDelegate.setDefaultNightMode(it)
        }
    }

    private fun destroyService(){
        val intent = Intent(this, MusicPlayerService::class.java)
        stopService(intent)
    }

    private fun setupPlayerService(){
        val intent = Intent(this, MusicPlayerService::class.java)
        startForegroundService(intent)
    }

    private fun disableInputAdjustment(){
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
    }
}
