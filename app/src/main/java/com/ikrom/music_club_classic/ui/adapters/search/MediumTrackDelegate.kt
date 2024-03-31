package com.ikrom.music_club_classic.ui.adapters.search

import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ikrom.music_club_classic.R
import com.ikrom.music_club_classic.data.model.Track
import com.ikrom.music_club_classic.extensions.getNames
import com.ikrom.music_club_classic.ui.adapters.base_adapters.BaseDelegateAdapter
import com.ikrom.music_club_classic.ui.adapters.base_adapters.IDelegateItem

data class TrackDelegateItem(
    val item: Track
) : IDelegateItem


class MediumTrackDelegate(
    val onMoreClick: (Track) -> Unit
): BaseDelegateAdapter<TrackDelegateItem, MediumTrackDelegate.TrackDelegateViewHolder>(
    TrackDelegateItem::class.java
) {
    inner class TrackDelegateViewHolder(itemView: View): DelegateViewHolder<TrackDelegateItem>(itemView) {
        val cover: ImageView = itemView.findViewById(R.id.iv_thumbnail)
        val titleTextView: TextView = itemView.findViewById(R.id.tv_title)
        val subtitleTextView: TextView = itemView.findViewById(R.id.tv_subtitle)
        val moreButton: ImageButton = itemView.findViewById(R.id.ib_more)

        override fun bind(item: TrackDelegateItem) {
            val track = item.item
            Glide
                .with(itemView.context)
                .load(track.album.thumbnail)
                .into(cover)
            titleTextView.text = track.title
            subtitleTextView.text= track.album.artists.getNames()
            moreButton.setImageResource(R.drawable.ic_more_vertical)
            moreButton.setOnClickListener {
                onMoreClick(track)
            }
        }
    }

    override fun createViewHolder(binding: View): RecyclerView.ViewHolder {
        return TrackDelegateViewHolder(binding)
    }

    override fun getLayoutId(): Int {
        return R.layout.layout_medium_covers
    }
}