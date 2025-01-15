package ru.ikrom.adapter_delegates.delegates

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.ikrom.adapter_delegates.R
import ru.ikrom.adapter_delegates.base.ThumbnailItem
import ru.ikrom.base_adapter.AdapterItem
import ru.ikrom.base_adapter.DelegateAdapter

data class ThumbnailRoundedSmallItem(
    override val id: String,
    override val title: String,
    override val subtitle: String,
    override val thumbnail: String,
): ThumbnailItem, AdapterItem


class ThumbnailRoundedSmallDelegate(
    onClick: (ThumbnailRoundedSmallItem) -> Unit,
    onLongClick: (ThumbnailRoundedSmallItem) -> Unit,
): DelegateAdapter<ThumbnailRoundedSmallItem, ThumbnailRoundedSmallDelegate.DelegateViewHolder>(
    ThumbnailRoundedSmallItem::class.java, onClick, onLongClick
) {
    inner class DelegateViewHolder(itemView: View): ViewHolder<ThumbnailRoundedSmallItem>(itemView) {
        private val ivThumbnail: ImageView = itemView.findViewById(R.id.iv_thumbnail)
        private val tvTitle: TextView = itemView.findViewById(R.id.tv_title)

        override fun bind(item: ThumbnailRoundedSmallItem) {
            Glide
                .with(itemView.context)
                .load(item.thumbnail)
                .centerCrop()
                .into(ivThumbnail)
            tvTitle.text = item.title
        }
    }

    override fun getViewHolder(binding: View): RecyclerView.ViewHolder {
        return DelegateViewHolder(binding)
    }

    override fun getLayoutId(): Int {
        return R.layout.item_thumbnail_small_rounded
    }
}