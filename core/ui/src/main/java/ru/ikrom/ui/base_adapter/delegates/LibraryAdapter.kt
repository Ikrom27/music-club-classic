package ru.ikrom.ui.base_adapter.delegates

import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import ru.ikrom.ui.R
import ru.ikrom.ui.base_adapter.DelegateAdapter

data class LibraryItem(
    val id: String,
    val title: String,
    val subtitle: String,
    val thumbnail: String,
)

class LibraryAdapter(
    val onClick: (LibraryItem) -> Unit,
    val onLongClick: (LibraryItem) -> Unit
): DelegateAdapter<LibraryItem, LibraryAdapter.LibraryViewHolder>(
    LibraryItem::class.java, onClick, onLongClick) {
    override fun getViewHolder(binding: View): ViewHolder<LibraryItem> {
        return LibraryViewHolder(binding)
    }

    override fun getLayoutId(): Int {
        return R.layout.item_library_playlist
    }

    inner class LibraryViewHolder(itemView: View): ViewHolder<LibraryItem>(itemView){
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
            btnMore.setOnClickListener{ onLongClick(item) }
        }

    }
}