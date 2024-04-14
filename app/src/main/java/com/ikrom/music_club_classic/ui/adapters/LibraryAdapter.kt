package com.ikrom.music_club_classic.ui.adapters

import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.ikrom.music_club_classic.R
import com.ikrom.music_club_classic.data.model.PlayList
import com.ikrom.music_club_classic.ui.adapters.base_adapters.AdapterItem
import com.ikrom.music_club_classic.ui.adapters.base_adapters.BaseAdapter

data class LibraryItem(
    val title: String,
    val subtitle: String,
    val thumbnail: String,
    val onButtonClick: () -> Unit,
    override val onClick: () -> Unit
) : AdapterItem()

class LibraryAdapter: BaseAdapter<LibraryItem>() {
    override fun getViewHolder(binding: View): BaseViewHolder<LibraryItem> {
        return LibraryViewHolder(binding)
    }

    override fun getLayoutId(): Int {
        return R.layout.item_library_playlist
    }

    inner class LibraryViewHolder(itemView: View): BaseViewHolder<LibraryItem>(itemView){
        private val coverView: ImageView = itemView.findViewById(R.id.iv_playlist_cover)
        private val titleView: TextView = itemView.findViewById(R.id.tv_title)
        private val subTitleView: TextView = itemView.findViewById(R.id.tv_subtitle)
        private val button: ImageButton = itemView.findViewById(R.id.ib_more)

        override fun bind(item: LibraryItem) {
            Glide
                .with(itemView.context)
                .load(item.thumbnail)
                .into(coverView)
            titleView.text = item.title
            subTitleView.text = item.subtitle
            button.setOnClickListener{ item.onButtonClick }
        }

    }
}