package ru.ikrom.theme

import android.content.res.Resources

fun Resources.appBottomMargin() = getDimensionPixelSize(AppDimens.HEIGHT_MINI_PLAYER) +
                                  getDimensionPixelSize(AppDimens.HEIGHT_BOTTOM_NAVBAR)

fun Resources.appTopMargin() = getDimensionPixelSize(AppDimens.HEIGHT_APP_BAR)