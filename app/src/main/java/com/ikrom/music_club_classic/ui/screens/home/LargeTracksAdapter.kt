package com.ikrom.music_club_classic.ui.screens.home

import android.annotation.SuppressLint
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.ikrom.music_club_classic.R
import com.ikrom.music_club_classic.data.model.Track
import com.ikrom.music_club_classic.extensions.getNames
import com.ikrom.music_club_classic.ui.base_adapters.BaseAdapter

class LargeTracksAdapter: BaseAdapter<Track>() {
    override fun getViewHolder(binding: View): BaseViewHolder<Track> {
        return TrackViewHolder(binding)
    }

    override fun getLayoutId(): Int {
        return R.layout.item_large_tracks
    }

    inner class TrackViewHolder(itemView: View): BaseViewHolder<Track>(itemView) {
        private val trackCover = itemView.findViewById<ImageView>(R.id.iv_track_cover)
        private val trackTitle = itemView.findViewById<TextView>(R.id.tv_title)
        private val trackAuthor = itemView.findViewById<TextView>(R.id.tv_subtitle)
        @SuppressLint("CheckResult")
        override fun bind(item: Track) {
            Glide
                .with(itemView.context)
                .load(item.album.cover)
                .centerCrop()
                .into(trackCover)
            trackTitle.text = item.title
            trackAuthor.text = item.album.artists.getNames()
        }

    }
}