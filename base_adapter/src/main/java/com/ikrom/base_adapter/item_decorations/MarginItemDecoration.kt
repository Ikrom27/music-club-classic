package com.ikrom.base_adapter.item_decorations

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class MarginItemDecoration(
    private val startSpace: Int = 0,
    private val endSpace: Int = 0,
    private var betweenSpace: Int = 0,
    private val isHorizontal: Boolean = false): RecyclerView.ItemDecoration() {

    init {
        betweenSpace /= 2
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val size = parent.adapter?.itemCount ?: 0
        val itemPosition =  parent.getChildAdapterPosition(view)
        if (isHorizontal) {
            outRect.left = getStartSpace(itemPosition)
            outRect.right = getEndSpace(itemPosition, size)
        } else {
            outRect.top = getStartSpace(itemPosition)
            outRect.bottom = getEndSpace(itemPosition, size)
        }
    }

    private fun getStartSpace(
        itemPosition: Int
    ): Int = when (itemPosition) {
        0 -> startSpace
        else -> betweenSpace
    }

    private fun getEndSpace(
        itemPosition: Int,
        size: Int,
    ): Int = when (itemPosition) {
        size-1 -> endSpace
        else -> betweenSpace
    }
}