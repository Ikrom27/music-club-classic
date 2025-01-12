package ru.ikrom.ui.base_adapter.item_decorations

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class GridSpacingItemDecoration(
    private val verticalSpacing: Int,
    private val horizontalSpacing: Int,
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        outRect.left = horizontalSpacing
        outRect.top = verticalSpacing / 2
        outRect.bottom = verticalSpacing / 2
    }
}


