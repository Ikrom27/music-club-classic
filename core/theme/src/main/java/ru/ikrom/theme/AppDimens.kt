package ru.ikrom.theme

import android.content.Context

object AppDimens {

    val mediumTrackCover = R.dimen.medium_track_cover
    val buttonSize = R.dimen.button_size

    // Corner radius
    val cornerRadiusRound = R.dimen.corner_radius_round
    val cornerRadiusExtraLarge = R.dimen.corner_radius_extra_large
    val cornerRadiusLarge = R.dimen.corner_radius_large
    val cornerRadiusMedium = R.dimen.corner_radius_medium
    val cornerRadiusSmall = R.dimen.corner_radius_small

    // Heights
    val miniPlayerHeight = R.dimen.mini_player_height
    val bottomNavBarHeight = R.dimen.bottom_nav_bar_height
    val appBarHeight = R.dimen.app_bar_height

    // Icon sizes
    val iconExtraSmall = R.dimen.icon_extra_small
    val iconSmall = R.dimen.icon_small
    val iconSmallPlus = R.dimen.icon_small_plus
    val iconMedium = R.dimen.icon_medium
    val iconMediumPlus = R.dimen.icon_medium_plus
    val iconLarge = R.dimen.icon_large
    val iconExtraLarge = R.dimen.icon_extra_large

    // Cover sizes
    val thumbnailMedium = R.dimen.thumbnail_medium
    val thumbnailSmallPlus = R.dimen.thumbnail_small_plus
    val thumbnailMediumPlus = R.dimen.thumbnail_medium_plus
    val thumbnailLarge = R.dimen.thumbnail_large

    // Placeholder
    val placeholderMedium = R.dimen.placeholder_medium

    // Margins
    val sectionMargin = R.dimen.section_margin
    val mediumItemsMargin = R.dimen.medium_items_margin
    val sectionTitleMargin = R.dimen.section_title_margin
    val itemsMargin = R.dimen.items_margin
    val contentHorizontalMargin = R.dimen.content_horizontal_margin
    val swipeRefreshStartMargin = R.dimen.swipe_refresh_start_margin
    val swipeRefreshEndMargin = R.dimen.swipe_refresh_end_margin

    // Spacers
    val spacerLarge = R.dimen.spacer_large
    val spacerMedium = R.dimen.spacer_medium
    val spacerSmall = R.dimen.spacer_small
    val spacerSmallExtra = R.dimen.spacer_small_extra
    val spacerExtraLargeDouble = R.dimen.spacer_extra_large_double

    fun Int.toDp(context: Context): Float {
        return context.resources.getDimension(this)
    }
}
