package com.ikrom.music_club_classic.extensions

import com.ikrom.music_club_classic.data.model.Track
import com.ikrom.music_club_classic.ui.adapters.delegates.MediumTrackItem


fun Track.toMediumTrackItem(
    onItemClick: () -> Unit,
    onButtonClick: () -> Unit,
): MediumTrackItem{
    return MediumTrackItem(
        title = this.title,
        subtitle = this.album.artists.getNames(),
        thumbnail = this.album.thumbnail,
        onItemClick = {onItemClick()},
        onButtonClick = {onButtonClick()}
    )
}