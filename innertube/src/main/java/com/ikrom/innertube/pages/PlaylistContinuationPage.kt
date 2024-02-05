package com.ikrom.innertube.pages

import com.ikrom.innertube.models.SongItem

data class PlaylistContinuationPage(
    val songs: List<SongItem>,
    val continuation: String?,
)
