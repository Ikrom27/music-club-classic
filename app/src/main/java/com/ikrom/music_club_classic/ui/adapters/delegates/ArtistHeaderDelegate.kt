package com.ikrom.music_club_classic.ui.adapters.delegates

import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ikrom.music_club_classic.R
import ru.ikrom.ui.base_adapter.DelegateAdapter
import ru.ikrom.ui.base_adapter.model.AdapterItem

data class ArtistHeaderItem(
    val title: String,
    val subtitle: String,
    val thumbnail: String,
    val onPlayClick: () -> Unit,
    val onShuffleClick: () -> Unit
): AdapterItem()

class ArtistHeaderDelegate: DelegateAdapter<ArtistHeaderItem, ArtistHeaderDelegate.ThumbnailLargeHeaderViewHolder>(
    ArtistHeaderItem::class.java
) {
    inner class ThumbnailLargeHeaderViewHolder(itemView: View):
    ViewHolder<ArtistHeaderItem>(itemView){
        private val thumbnail: ImageView = itemView.findViewById(R.id.iv_thumbnail)
        private val title: TextView = itemView.findViewById(R.id.tv_title)
        private val btnPlayAll: Button = itemView.findViewById(R.id.btn_play_all)
        private val btnPlayShuffled: Button = itemView.findViewById(R.id.btn_play_shuffled)
        override fun bind(item: ArtistHeaderItem) {
            Glide
                .with(itemView.context)
                .load(item.thumbnail)
                .centerCrop()
                .into(thumbnail)
            title.text = item.title
            setupButtons(item)
        }

        private fun setupButtons(item: ArtistHeaderItem) {
            btnPlayAll.setOnClickListener{
                item.onPlayClick()
            }
            btnPlayShuffled.setOnClickListener{
                item.onShuffleClick()
            }
        }
    }

    override fun getViewHolder(binding: View): RecyclerView.ViewHolder {
        return ThumbnailLargeHeaderViewHolder(binding)
    }

    override fun getLayoutId(): Int {
        return R.layout.item_artist_header
    }
}