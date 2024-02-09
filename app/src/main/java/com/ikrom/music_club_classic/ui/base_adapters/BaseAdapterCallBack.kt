package com.ikrom.music_club_classic.ui.base_adapters

import android.view.View

abstract class BaseAdapterCallBack<T> {
    abstract fun onItemClick(item: T, view: View)
    open fun onLongClick(item: T, view: View) {}
}