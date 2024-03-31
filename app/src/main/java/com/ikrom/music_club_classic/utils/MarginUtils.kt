package com.ikrom.music_club_classic.utils

import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams

fun setupMarginFromStatusBar(view: View){
    ViewCompat.setOnApplyWindowInsetsListener(view){ v, windowInsets ->
        val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
        v.updateLayoutParams<ViewGroup.MarginLayoutParams> {
            topMargin = insets.top
        }
        WindowInsetsCompat.CONSUMED
    }
}