package com.ikrom.music_club_classic.ui.adapters.playlist

import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ikrom.music_club_classic.R
import com.ikrom.music_club_classic.data.model.Track
import com.ikrom.music_club_classic.extensions.getNames
import com.ikrom.music_club_classic.ui.adapters.base_adapters.BaseAdapter
import com.ikrom.music_club_classic.ui.adapters.base_adapters.BaseDelegateAdapter
import com.ikrom.music_club_classic.ui.adapters.base_adapters.IDelegateItem

data class PlaylistTrackDelegateItem(
    val track: Track
) : IDelegateItem

class PlaylistTrackAdapter(
    val onItemClick: (Track) -> Unit,
    val onMoreButtonClick: (Track) -> Unit
): BaseDelegateAdapter<PlaylistTrackDelegateItem, BaseDelegateAdapter.DelegateViewHolder<PlaylistTrackDelegateItem>>(
    PlaylistTrackDelegateItem::class.java
) {
    inner class PlaylistTrackHolder(itemView: View): DelegateViewHolder<PlaylistTrackDelegateItem>(itemView) {
        val container: View = itemView.findViewById(R.id.container)
        val thumbnail: ImageView = itemView.findViewById(R.id.iv_thumbnail)
        val title: TextView = itemView.findViewById(R.id.tv_title)
        val subtitle: TextView = itemView.findViewById(R.id.tv_subtitle)
        val moreButton: ImageButton = itemView.findViewById(R.id.ib_more)

        override fun bind(item: PlaylistTrackDelegateItem) {
            Glide
                .with(itemView.context)
                .load(item.track.album.thumbnail)
                .centerCrop()
                .into(thumbnail)
            title.text = item.track.title
            subtitle.text = item.track.album.artists.getNames()
            setupButtons(item.track)
        }

        private fun setupButtons(track: Track){
            moreButton.setOnClickListener {
                onMoreButtonClick(track)
                Log.d("click", "boob")
            }
            container.setOnClickListener{
                onItemClick(track)
            }
        }
    }

    override fun createViewHolder(binding: View): RecyclerView.ViewHolder {
        return PlaylistTrackHolder(binding)
    }

    override fun getLayoutId(): Int {
        return R.layout.layout_medium_covers
    }
}