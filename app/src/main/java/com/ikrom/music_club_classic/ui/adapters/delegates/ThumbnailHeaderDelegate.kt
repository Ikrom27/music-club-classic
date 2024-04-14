package com.ikrom.music_club_classic.ui.adapters.delegates

import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ikrom.music_club_classic.R
import com.ikrom.music_club_classic.ui.adapters.base_adapters.BaseDelegateAdapter
import com.ikrom.music_club_classic.ui.adapters.base_adapters.AdapterItem

data class ThumbnailHeaderItem(
    val title: String,
    val subtitle: String,
    val thumbnail: String,
    val onPlayClick: () -> Unit,
    val onShuffleClick: () -> Unit
) : AdapterItem()

class ThumbnailHeaderDelegate
    : BaseDelegateAdapter<ThumbnailHeaderItem, ThumbnailHeaderDelegate.ThumbnailViewHolder>(
    ThumbnailHeaderItem::class.java
    ) {
    inner class ThumbnailViewHolder(itemView: View)
        : DelegateViewHolder<ThumbnailHeaderItem>(itemView) {
        private val thumbnail: ImageView = itemView.findViewById(R.id.iv_thumbnail_view)
        private val title: TextView = itemView.findViewById(R.id.tv_title)
        private val author: TextView = itemView.findViewById(R.id.tv_author)
        private val btnPlayAll: Button = itemView.findViewById(R.id.btn_play_all)
        private val btnPlayShuffled: Button = itemView.findViewById(R.id.btn_play_shuffled)

        override fun bind(item: ThumbnailHeaderItem) {
            Glide
                .with(itemView.context)
                .load(item.thumbnail)
                .into(thumbnail)
            title.text = item.title
            author.text = item.subtitle
            setupButtons(item)
        }

        private fun setupButtons(item: ThumbnailHeaderItem) {
            btnPlayAll.setOnClickListener{
                item.onPlayClick()
            }
            btnPlayShuffled.setOnClickListener{
                item.onShuffleClick()
            }
        }
    }

    override fun createViewHolder(binding: View): RecyclerView.ViewHolder {
        return ThumbnailViewHolder(binding)
    }

    override fun getLayoutId(): Int {
        return R.layout.item_album_header
    }

}