package ru.ikrom.ui.base_adapter.delegates

import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.ikrom.ui.R
import ru.ikrom.ui.base_adapter.DelegateAdapter

data class ThumbnailSmallItem(
    override val id: String,
    override val title: String,
    override val subtitle: String,
    override val thumbnail: String,
): ThumbnailItem


class ThumbnailSmallDelegate(
    onClick: (ThumbnailSmallItem) -> Unit,
    val onLongClick: (ThumbnailSmallItem) -> Unit,
): DelegateAdapter<ThumbnailSmallItem, ThumbnailSmallDelegate.ThumbnailSmallViewHolder>(
    ThumbnailSmallItem::class.java, onClick, onLongClick
) {
    inner class ThumbnailSmallViewHolder(itemView: View): ViewHolder<ThumbnailSmallItem>(itemView) {
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
            btnMore.setOnClickListener { onLongClick(item) }
        }
    }

    override fun getViewHolder(binding: View): RecyclerView.ViewHolder {
        return ThumbnailSmallViewHolder(binding)
    }

    override fun getLayoutId(): Int {
        return R.layout.item_thumbnail_small
    }
}