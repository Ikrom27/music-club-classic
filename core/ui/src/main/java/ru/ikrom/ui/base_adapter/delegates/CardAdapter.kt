package ru.ikrom.ui.base_adapter.delegates

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import jp.wasabeef.glide.transformations.BlurTransformation
import ru.ikrom.ui.R
import ru.ikrom.ui.base_adapter.DelegateAdapter

data class CardItem(
    override val id: String,
    override val title: String,
    override val subtitle: String,
    override val thumbnail: String,
): ThumbnailItem

class CardAdapter(
    onClick: (CardItem) -> Unit,
    onLongClick: (CardItem) -> Unit
): DelegateAdapter<CardItem, CardAdapter.CardViewHolder>(
    CardItem::class.java, onClick, onLongClick
) {
    inner class CardViewHolder(itemView: View): ViewHolder<CardItem>(itemView){
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

    override fun getViewHolder(binding: View): ViewHolder<CardItem> {
        return CardViewHolder(binding)
    }

    override fun getLayoutId(): Int {
        return R.layout.item_card_thumbnail
    }
}