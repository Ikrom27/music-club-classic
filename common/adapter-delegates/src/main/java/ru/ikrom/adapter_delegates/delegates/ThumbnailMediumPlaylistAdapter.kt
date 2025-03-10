package ru.ikrom.adapter_delegates.delegates

import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import ru.ikrom.adapter_delegates.R
import ru.ikrom.base_adapter.AdapterItem
import ru.ikrom.base_adapter.DelegateAdapter
import ru.ikrom.base_adapter.ThumbnailItem
import ru.ikrom.theme.AppDrawableIds


data class ThumbnailMediumPlaylistItem(
    override val id: String,
    override val title: String,
    override val subtitle: String,
    override val thumbnail: String,
): AdapterItem, ThumbnailItem {
    fun filterWithText(text: String) = text.uppercase() in title.uppercase()
}


class ThumbnailMediumPlaylistAdapter(
    val onClick: (ThumbnailMediumPlaylistItem) -> Unit,
    val onLongClick: (ThumbnailMediumPlaylistItem) -> Unit
): DelegateAdapter<ThumbnailMediumPlaylistItem, ThumbnailMediumPlaylistAdapter.AdapterViewHolder>(
    ThumbnailMediumPlaylistItem::class.java, onClick, onLongClick) {
    override fun getViewHolder(binding: View): ViewHolder<ThumbnailMediumPlaylistItem> {
        return AdapterViewHolder(binding)
    }

    override fun getLayoutId(): Int {
        return R.layout.item_library_playlist
    }

    inner class AdapterViewHolder(itemView: View): ViewHolder<ThumbnailMediumPlaylistItem>(itemView){
        private val ivThumbnail: ImageView = itemView.findViewById(R.id.iv_thumbnail)
        private val tvTitle: TextView = itemView.findViewById(R.id.tv_title)
        private val tvSubtitle: TextView = itemView.findViewById(R.id.tv_subtitle)
        private val btnMore: ImageButton = itemView.findViewById(R.id.ib_more)

        override fun bind(item: ThumbnailMediumPlaylistItem) {
            tvTitle.text = item.title
            Glide
                .with(itemView.context)
                .load(item.thumbnail)
                .centerCrop()
                .placeholder(AppDrawableIds.PH_DISK)
                .into(ivThumbnail)
            tvSubtitle.text = item.subtitle
            btnMore.setOnClickListener{ onLongClick(item) }
        }

    }
}