package com.ikrom.music_club_classic.ui.adapters.delegates

import android.annotation.SuppressLint
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.ikrom.music_club_classic.R
import com.ikrom.base_adapter.model.AdapterItem

data class LargeThumbnailItem(
    val title: String,
    val subtitle: String,
    val thumbnail: String,
    override val onClick: () -> Unit,
    override val onLongClick: () -> Unit
) : AdapterItem()

class LargeTracksAdapter: com.ikrom.base_adapter.BaseAdapter<LargeThumbnailItem>() {
    override fun getViewHolder(binding: View): BaseViewHolder<LargeThumbnailItem> {
        return TrackViewHolder(binding)
    }

    override fun getLayoutId(): Int {
        return R.layout.item_large_tracks
    }

    inner class TrackViewHolder(itemView: View): BaseViewHolder<LargeThumbnailItem>(itemView) {
        private val trackCover = itemView.findViewById<ImageView>(R.id.iv_track_cover)
        private val trackTitle = itemView.findViewById<TextView>(R.id.tv_title)
        private val trackAuthor = itemView.findViewById<TextView>(R.id.tv_subtitle)
        @SuppressLint("CheckResult")
        override fun bind(item: LargeThumbnailItem) {
            Glide
                .with(itemView.context)
                .load(item.thumbnail)
                .centerCrop()
                .into(trackCover)
            trackTitle.text = item.title
            trackAuthor.text = item.subtitle
        }

    }
}