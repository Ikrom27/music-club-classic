package ru.ikrom.utils

import android.content.Context
import android.content.Intent

object ActionUtil {
    fun shareIntent(context: Context, link: String){
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, link)
            type = "text/plain"
        }
        context.startActivity(Intent.createChooser(sendIntent, "Share"))
    }
}