package ru.ikrom.adapter_delegates.delegates

import android.annotation.SuppressLint
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import ru.ikrom.adapter_delegates.R
import ru.ikrom.base_adapter.ThumbnailItem
import ru.ikrom.base_adapter.AdapterItem
import ru.ikrom.base_adapter.DelegateAdapter

data class ThumbnailLargeItem(
    override val id: String,
    override val title: String,
    override val subtitle: String,
    override val thumbnail: String
): ThumbnailItem, AdapterItem

class ThumbnailLargeAdapter(
    onClick: (ThumbnailLargeItem) -> Unit,
    onLongClick: (ThumbnailLargeItem) -> Unit
): DelegateAdapter<ThumbnailLargeItem, ThumbnailLargeAdapter.ThumbnailLargeViewHolder>(
    ThumbnailLargeItem::class.java, onClick, onLongClick
) {
    override fun getViewHolder(binding: View): ViewHolder<ThumbnailLargeItem> {
        return ThumbnailLargeViewHolder(binding)
    }

    override fun getLayoutId(): Int {
        return R.layout.item_thumbnail_large
    }

    inner class ThumbnailLargeViewHolder(itemView: View): ViewHolder<ThumbnailLargeItem>(itemView) {
        private val trackCover = itemView.findViewById<ImageView>(R.id.iv_track_cover)
        private val trackTitle = itemView.findViewById<TextView>(R.id.tv_title)
        private val trackAuthor = itemView.findViewById<TextView>(R.id.tv_subtitle)
        @SuppressLint("CheckResult")
        override fun bind(item: ThumbnailLargeItem) {
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