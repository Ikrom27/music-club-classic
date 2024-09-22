package com.ikrom.music_club_classic.ui.adapters.delegates

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.ikrom.music_club_classic.R
import jp.wasabeef.glide.transformations.BlurTransformation
import ru.ikrom.ui.base_adapter.BaseAdapter
import ru.ikrom.ui.base_adapter.model.AdapterItem

data class CardItem(
    val title: String,
    val subtitle: String,
    val thumbnail: String,
    override val onClick: () -> Unit = {},
    override val onLongClick: () -> Unit = {}
) : AdapterItem()

class CardAdapter: BaseAdapter<CardItem>() {
    inner class CardViewHolder(itemView: View): BaseViewHolder<CardItem>(itemView){
        private val ivThumbnail = itemView.findViewById<ImageView>(R.id.iv_thumbnail)
        private val ivThumbnailBackground = itemView.findViewById<ImageView>(R.id.iv_thumbnail_background)
        private val tvTitle = itemView.findViewById<TextView>(R.id.tv_title)
        private val tvSubtitle = itemView.findViewById<TextView>(R.id.tv_subtitle)
        override fun bind(item: CardItem) {
            Glide
                .with(itemView.context)
                .load(item.thumbnail)
                .into(ivThumbnail)
            Glide
                .with(itemView.context)
                .load(item.thumbnail)
                .centerCrop()
                .transform(BlurTransformation(128))
                .into(ivThumbnailBackground)
            tvTitle.text = item.title
            tvSubtitle.text = item.subtitle
        }
    }

    override fun getViewHolder(binding: View): BaseViewHolder<CardItem> {
        return CardViewHolder(binding)
    }

    override fun getLayoutId(): Int {
        return R.layout.item_card_thumbnail
    }
}