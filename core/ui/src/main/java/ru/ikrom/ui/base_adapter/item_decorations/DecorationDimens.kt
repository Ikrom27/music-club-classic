package ru.ikrom.ui.base_adapter.item_decorations

import android.content.res.Resources
import ru.ikrom.ui.R

object DecorationDimens {
    fun getBottomMargin(resources: Resources): Int{
        return resources.getDimensionPixelSize(R.dimen.mini_player_height)
    }

    fun getHorizontalMargin(resources: Resources): Int {
        return resources.getDimensionPixelSize(R.dimen.mini_player_height)
    }

    fun getSectionMargin(resources: Resources): Int {
        return resources.getDimensionPixelSize(R.dimen.section_margin)
    }
}