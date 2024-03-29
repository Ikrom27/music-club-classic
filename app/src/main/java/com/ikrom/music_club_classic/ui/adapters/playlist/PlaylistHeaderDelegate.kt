package com.ikrom.music_club_classic.ui.adapters.playlist

import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ikrom.music_club_classic.R
import com.ikrom.music_club_classic.data.model.PlayList
import com.ikrom.music_club_classic.data.model.Track
import com.ikrom.music_club_classic.ui.adapters.base_adapters.BaseDelegateAdapter
import com.ikrom.music_club_classic.ui.adapters.base_adapters.IDelegateItem

data class PlaylistHeaderDelegateItem(
    val playlist: PlayList
) : IDelegateItem

class PlaylistHeaderDelegate(
    val onPlayClick: () -> Unit,
    val onShuffleClick: () -> Unit
)
    : BaseDelegateAdapter<PlaylistHeaderDelegateItem, PlaylistHeaderDelegate.PlaylistDelegateViewHolder>(
    PlaylistHeaderDelegateItem::class.java
    ) {
    inner class PlaylistDelegateViewHolder(itemView: View)
        : DelegateViewHolder<PlaylistHeaderDelegateItem>(itemView) {
        private val thumbnail: ImageView = itemView.findViewById(R.id.iv_thumbnail_view)
        private val title: TextView = itemView.findViewById(R.id.tv_title)
        private val author: TextView = itemView.findViewById(R.id.tv_author)
        private val btnPlayAll: Button = itemView.findViewById(R.id.btn_play_all)
        private val btnPlayShuffled: Button = itemView.findViewById(R.id.btn_play_shuffled)

        override fun bind(item: PlaylistHeaderDelegateItem) {
            val playlist = item.playlist
            Glide
                .with(itemView.context)
                .load(playlist.thumbnail)
                .into(thumbnail)
            title.text = playlist.title
            author.text = playlist.artists?.name ?: "unknown author"
            setupButtons()
        }

        private fun setupButtons(){
            btnPlayAll.setOnClickListener{
                onPlayClick()
            }
            btnPlayShuffled.setOnClickListener{
                onShuffleClick()
            }
        }
    }

    override fun createViewHolder(binding: View): RecyclerView.ViewHolder {
        return PlaylistDelegateViewHolder(binding)
    }

    override fun getLayoutId(): Int {
        return R.layout.item_album_header
    }

}