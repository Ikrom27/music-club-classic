package com.ikrom.music_club_classic.ui.adapters.delegates

import android.annotation.SuppressLint
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.ikrom.music_club_classic.R
import ru.ikrom.ui.base_adapter.DelegateAdapter

data class ThumbnailMediumItem(
    val id: String,
    val title: String,
    val subtitle: String,
    val thumbnail: String,
)

class ThumbnailMediumAdapter(
    onClick: (ThumbnailMediumItem) -> Unit,
    onLongClick: (ThumbnailMediumItem) -> Unit
): DelegateAdapter<ThumbnailMediumItem, ThumbnailMediumAdapter.ThumbnailMediumViewHolder>(
    ThumbnailMediumItem::class.java, onClick, onLongClick
) {
    override fun getViewHolder(binding: View): ViewHolder<ThumbnailMediumItem> {
        return ThumbnailMediumViewHolder(binding)
    }

    override fun getLayoutId(): Int {
        return R.layout.item_thumbnail_medium
    }

    inner class ThumbnailMediumViewHolder(itemView: View): ViewHolder<ThumbnailMediumItem>(itemView) {
        private val trackCover = itemView.findViewById<ImageView>(R.id.iv_track_cover)
        private val trackTitle = itemView.findViewById<TextView>(R.id.tv_title)
        private val trackAuthor = itemView.findViewById<TextView>(R.id.tv_subtitle)
        @SuppressLint("CheckResult")
        override fun bind(item: ThumbnailMediumItem) {
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