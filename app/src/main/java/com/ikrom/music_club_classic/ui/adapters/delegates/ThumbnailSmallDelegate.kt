package com.ikrom.music_club_classic.ui.adapters.delegates

import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ikrom.music_club_classic.R
import ru.ikrom.ui.BaseDelegateAdapter
import ru.ikrom.ui.model.AdapterItem

data class ThumbnailSmallItem(
    val title: String,
    val subtitle: String,
    val thumbnail: String,
    val onButtonClick: () -> Unit,
    override val onClick: () -> Unit,
    override val onLongClick: () -> Unit
) : AdapterItem()


class ThumbnailSmallDelegate:
    BaseDelegateAdapter<ThumbnailSmallItem, ThumbnailSmallDelegate.ThumbnailSmallViewHolder>(
    ThumbnailSmallItem::class.java
) {
    inner class ThumbnailSmallViewHolder(itemView: View): DelegateViewHolder<ThumbnailSmallItem>(itemView) {
        private val ivThumbnail: ImageView = itemView.findViewById(R.id.iv_thumbnail)
        private val tvTitle: TextView = itemView.findViewById(R.id.tv_title)
        private val tvSubtitle: TextView = itemView.findViewById(R.id.tv_subtitle)
        private val btnMore: ImageButton = itemView.findViewById(R.id.ib_more)

        override fun bind(item: ThumbnailSmallItem) {
            Glide
                .with(itemView.context)
                .load(item.thumbnail)
                .into(ivThumbnail)
            tvTitle.text = item.title
            tvSubtitle.text= item.subtitle
            btnMore.setOnClickListener { item.onButtonClick() }
        }
    }

    override fun createViewHolder(binding: View): RecyclerView.ViewHolder {
        return ThumbnailSmallViewHolder(binding)
    }

    override fun getLayoutId(): Int {
        return R.layout.item_thumbnail_small
    }
}