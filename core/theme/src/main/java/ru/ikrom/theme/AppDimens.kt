package ru.ikrom.theme

import android.content.Context

object AppDimens {
    val MEDIUM_TRACK_COVER = R.dimen.medium_track_cover
    val BUTTON_SIZE = R.dimen.button_size

    // Corner radius
    val CORNER_RADIUS_ROUND = R.dimen.corner_radius_round
    val CORNER_RADIUS_EXTRA_LARGE = R.dimen.corner_radius_extra_large
    val CORNER_RADIUS_LARGE = R.dimen.corner_radius_large
    val CORNER_RADIUS_MEDIUM = R.dimen.corner_radius_medium
    val CORNER_RADIUS_SMALL = R.dimen.corner_radius_small

    // Heights
    val HEIGHT_MINI_PLAYER = R.dimen.mini_player_height
    val HEIGHT_BOTTOM_NAVBAR = R.dimen.bottom_nav_bar_height
    val HEIGHT_APP_BAR = R.dimen.app_bar_height

    // Icon sizes
    val ICON_EXTRA_SMALL = R.dimen.icon_extra_small
    val ICON_SMALL = R.dimen.icon_small
    val ICON_SMALL_PLUS = R.dimen.icon_small_plus
    val ICON_MEDIUM = R.dimen.icon_medium
    val ICON_MEDIUM_PLUS = R.dimen.icon_medium_plus
    val ICON_LARGE = R.dimen.icon_large
    val ICON_EXTRA_LARGE = R.dimen.icon_extra_large

    // Cover sizes
    val THUMBNAIL_MEDIUM = R.dimen.thumbnail_medium
    val THUMBNAIL_SMALL_PLUS = R.dimen.thumbnail_small_plus
    val THUMBNAIL_MEDIUM_PLUS = R.dimen.thumbnail_medium_plus
    val THUMBNAIL_LARGE = R.dimen.thumbnail_large

    // Placeholder
    val PLACEHOLDER_MEDIUM = R.dimen.placeholder_medium

    // Margins
    val MARGIN_SECTIONS = R.dimen.section_margin
    val MEDIUM_ITEMS_MARGIN = R.dimen.medium_items_margin
    val SECTION_TITLE_MARGIN = R.dimen.section_title_margin
    val MARGIN_ITEMS = R.dimen.items_margin
    val MARGIN_CONTENT_HORIZONTAL = R.dimen.content_horizontal_margin
    val MARGIN_REFRESH_START = R.dimen.swipe_refresh_start_margin
    val MARGIN_REFRESH_END = R.dimen.swipe_refresh_end_margin

    // Spacers
    val SPACER_LARGE = R.dimen.spacer_large
    val SPACER_MEDIUM = R.dimen.spacer_medium
    val SPACER_SMALL = R.dimen.spacer_small
    val SPACER_SMALL_EXTRA = R.dimen.spacer_small_extra
    val SPACER_EXTRA_LARGE_DOUBLE = R.dimen.spacer_extra_large_double

    fun Int.toDp(context: Context): Float {
        return context.resources.getDimension(this)
    }
}
