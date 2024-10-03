package com.ikrom.music_club_classic.data.data_source

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import ru.ikrom.theme.AppStringsId
import javax.inject.Inject

class SettingsDataSource @Inject constructor(
    context: Context
) {
    private val PREFERENCES_KEY = context.getString(AppStringsId.preferencesFileKey)
    private val PREFERENCES_THEME_MODE = context.getString(AppStringsId.preferencesThemeMode)

    private val sharedPreferences = context.getSharedPreferences(PREFERENCES_KEY, Context.MODE_PRIVATE)
    fun saveThemeSettings(themeMode: Int){
        with (sharedPreferences.edit()) {
            putInt(PREFERENCES_THEME_MODE, themeMode)
            apply()
        }
    }

    fun loadThemeSettings(): Int {
        return sharedPreferences.getInt(PREFERENCES_THEME_MODE, AppCompatDelegate.MODE_NIGHT_YES)
    }
}