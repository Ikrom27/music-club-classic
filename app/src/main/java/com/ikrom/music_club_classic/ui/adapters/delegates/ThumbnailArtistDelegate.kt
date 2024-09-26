package com.ikrom.music_club_classic.ui.adapters.delegates

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.ikrom.music_club_classic.R
import ru.ikrom.ui.base_adapter.DelegateAdapter

data class ThumbnailRoundedItem(
    val id: String,
    val title: String,
    val thumbnail: String
)


class ThumbnailRoundedAdapter(
    onClick: (ThumbnailRoundedItem) -> Unit
):
    DelegateAdapter<ThumbnailRoundedItem, ThumbnailRoundedAdapter.ThumbnailRoundedViewHolder>(
        ThumbnailRoundedItem::class.java, onClick
    ) {
    inner class ThumbnailRoundedViewHolder(itemView: View): ViewHolder<ThumbnailRoundedItem>(itemView) {
        private val ivThumbnail: ImageView = itemView.findViewById(R.id.iv_thumbnail)
        private val tvTitle: TextView = itemView.findViewById(R.id.tv_title)

        override fun bind(item: ThumbnailRoundedItem) {
            Glide
                .with(itemView.context)
                .load(item.thumbnail)
                .into(ivThumbnail)
            tvTitle.text = item.title
        }
    }

    override fun getViewHolder(binding: View): ViewHolder<ThumbnailRoundedItem> {
        return ThumbnailRoundedViewHolder(binding)
    }

    override fun getLayoutId(): Int {
        return R.layout.item_thumbnail_round
    }
}