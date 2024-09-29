package com.ikrom.music_club_classic.extensions

fun Long.toTimeString(): String {
    val hours = this / 1000 / 3600
    val minutes = this / 1000 % 3600 / 60
    val seconds = this / 1000 % 60
    return when {
        hours > 0 -> String.format("%02d:%02d:%02d", hours, minutes, seconds)
        else -> String.format("%02d:%02d", minutes, seconds)
    }
}
