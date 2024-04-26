package com.ikrom.music_club_classic.data.repository

import com.ikrom.music_club_classic.data.data_source.SettingsDataSource
import javax.inject.Inject

class SettingsRepository @Inject constructor (
    private val settingsDataSource: SettingsDataSource
) {
    fun setThemeMode(themeMode: Int){
        settingsDataSource.saveThemeSettings(themeMode)
    }

    fun getThemeMode(): Int {
        return settingsDataSource.loadThemeSettings()
    }
}