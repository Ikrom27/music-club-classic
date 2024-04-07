package com.ikrom.music_club_classic.ui.adapters.delegates

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.ikrom.music_club_classic.R
import com.ikrom.music_club_classic.ui.adapters.base_adapters.BaseAdapter
import com.ikrom.music_club_classic.ui.adapters.base_adapters.BaseAdapterCallBack
import com.ikrom.music_club_classic.ui.adapters.base_adapters.IDelegateItem
import jp.wasabeef.glide.transformations.BlurTransformation

data class CardItem(
    val title: String,
    val subtitle: String,
    val thumbnail: String,
    val onItemClick: () -> Unit = {},
    val onLongClick: () -> Unit = {}
) : IDelegateItem

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
            setClickListener()
        }

        private fun setClickListener(){
            this@CardAdapter.attachCallBack(
                object : BaseAdapterCallBack<CardItem>() {
                    override fun onItemClick(item: CardItem, view: View) {
                        item.onItemClick()
                    }

                    override fun onLongClick(item: CardItem, view: View) {
                        item.onLongClick()
                    }
                }
            )
        }
    }

    override fun getViewHolder(binding: View): BaseViewHolder<CardItem> {
        return CardViewHolder(binding)
    }

    override fun getLayoutId(): Int {
        return R.layout.item_card_thumbnail
    }
}