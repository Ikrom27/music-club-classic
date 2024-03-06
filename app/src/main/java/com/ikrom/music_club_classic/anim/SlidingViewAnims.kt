package com.ikrom.music_club_classic.anim

import android.view.View

fun miniPlayerAlphaProgress(view: View, progress: Float){
    val threshold = 0.3f
    if (progress <= threshold) {
        view.alpha = 1f - progress / threshold
    } else {
        view.alpha = 0f
    }
}

fun playerContainerAlphaProgress(view: View, progress: Float){
    val threshold = 0.3f
    if (progress >= threshold) {
        val alpha = (progress - threshold) / (1 - threshold)
        view.alpha = alpha
    } else {
        view.alpha = 0f
    }
}