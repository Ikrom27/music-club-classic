package com.ikrom.music_club_classic.ui.adapters.delegates

import android.annotation.SuppressLint
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.ikrom.music_club_classic.R
import ru.ikrom.ui.BaseAdapter
import ru.ikrom.ui.model.AdapterItem

data class ThumbnailMediumItem(
    val title: String,
    val subtitle: String,
    val thumbnail: String,
    override val onClick: () -> Unit,
    override val onLongClick: () -> Unit
) : AdapterItem()

class ThumbnailMediumAdapter: BaseAdapter<ThumbnailMediumItem>() {
    override fun getViewHolder(binding: View): BaseViewHolder<ThumbnailMediumItem> {
        return ThumbnailMediumViewHolder(binding)
    }

    override fun getLayoutId(): Int {
        return R.layout.item_thumbnail_medium
    }

    inner class ThumbnailMediumViewHolder(itemView: View): BaseViewHolder<ThumbnailMediumItem>(itemView) {
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