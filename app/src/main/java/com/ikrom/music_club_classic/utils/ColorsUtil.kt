package com.ikrom.music_club_classic.utils

import android.graphics.Color
import androidx.core.graphics.ColorUtils

object ColorsUtil {
    fun darkenColor(color: Int, sat: Boolean = false): Int {
        val hsv = FloatArray(3)
        Color.colorToHSV(color, hsv)
        val luminance = ColorUtils.calculateLuminance(color)
        if (luminance > 0.4){
            hsv[2] *= 0.3f
        } else {
            hsv[2] *= 0.8f
        }
        if (hsv[1] > 0.5 && sat){
            hsv[1] *= 0.4f
        }
        return Color.HSVToColor(hsv)
    }
}