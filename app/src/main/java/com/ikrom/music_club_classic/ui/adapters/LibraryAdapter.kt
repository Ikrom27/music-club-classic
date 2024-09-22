package com.ikrom.music_club_classic.ui.adapters

import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.ikrom.music_club_classic.R
import ru.ikrom.ui.base_adapter.BaseAdapter
import ru.ikrom.ui.base_adapter.model.AdapterItem

data class LibraryItem(
    val title: String,
    val subtitle: String,
    val thumbnail: String,
    override val onClick: () -> Unit,
    override val onLongClick: () -> Unit,
) : AdapterItem()

class LibraryAdapter: BaseAdapter<LibraryItem>() {
    override fun getViewHolder(binding: View): BaseViewHolder<LibraryItem> {
        return LibraryViewHolder(binding)
    }

    override fun getLayoutId(): Int {
        return R.layout.item_library_playlist
    }

    inner class LibraryViewHolder(itemView: View): BaseViewHolder<LibraryItem>(itemView){
        private val ivThumbnail: ImageView = itemView.findViewById(R.id.iv_thumbnail)
        private val tvTitle: TextView = itemView.findViewById(R.id.tv_title)
        private val tvSubtitle: TextView = itemView.findViewById(R.id.tv_subtitle)
        private val btnMore: ImageButton = itemView.findViewById(R.id.ib_more)

        override fun bind(item: LibraryItem) {
            Glide
                .with(itemView.context)
                .load(item.thumbnail)
                .placeholder(R.drawable.ph_album)
                .into(ivThumbnail)
            tvTitle.text = item.title
            tvSubtitle.text = item.subtitle
            btnMore.setOnClickListener{ item.onLongClick }
        }

    }
}