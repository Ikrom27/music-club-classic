package com.ikrom.innertube.pages

import com.ikrom.innertube.models.ItemAlbum
import com.ikrom.innertube.models.ItemArtist
import com.ikrom.innertube.models.BrowseEndpoint
import com.ikrom.innertube.models.PlaylistPanelVideoRenderer
import com.ikrom.innertube.models.SongItem
import com.ikrom.innertube.models.WatchEndpoint
import com.ikrom.innertube.models.oddElements
import com.ikrom.innertube.models.splitBySeparator
import com.ikrom.innertube.utils.parseTime

data class NextResult(
    val title: String? = null,
    val items: List<SongItem>,
    val currentIndex: Int? = null,
    val lyricsEndpoint: BrowseEndpoint? = null,
    val relatedEndpoint: BrowseEndpoint? = null,
    val continuation: String?,
    val endpoint: WatchEndpoint, // current or continuation next endpoint
)

object NextPage {
    fun fromPlaylistPanelVideoRenderer(renderer: PlaylistPanelVideoRenderer): SongItem? {
        val longByLineRuns = renderer.longBylineText?.runs?.splitBySeparator() ?: return null
        return SongItem(
            id = renderer.videoId ?: return null,
            title = renderer.title?.runs?.firstOrNull()?.text ?: return null,
            itemArtists = longByLineRuns.firstOrNull()?.oddElements()?.map {
                ItemArtist(
                    name = it.text,
                    id = it.navigationEndpoint?.browseEndpoint?.browseId
                )
            } ?: return null,
            itemAlbum = longByLineRuns.getOrNull(1)?.firstOrNull()?.takeIf {
                it.navigationEndpoint?.browseEndpoint != null
            }?.let {
                ItemAlbum(
                    name = it.text,
                    id = it.navigationEndpoint?.browseEndpoint?.browseId!!
                )
            },
            duration = renderer.lengthText?.runs?.firstOrNull()?.text?.parseTime() ?: return null,
            thumbnail = renderer.thumbnail.thumbnails.lastOrNull()?.url ?: return null,
            explicit = renderer.badges?.find {
                it.musicInlineBadgeRenderer.icon.iconType == "MUSIC_EXPLICIT_BADGE"
            } != null
        )
    }
}
