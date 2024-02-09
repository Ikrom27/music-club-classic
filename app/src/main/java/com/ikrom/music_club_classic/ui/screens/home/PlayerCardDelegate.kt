package com.ikrom.music_club_classic.ui.screens.home

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ikrom.music_club_classic.R
import com.ikrom.music_club_classic.data.model.Track
import com.ikrom.music_club_classic.extensions.getNames
import com.ikrom.music_club_classic.ui.base_adapters.BaseDelegateAdapter
import com.ikrom.music_club_classic.ui.base_adapters.IDelegateItem

data class PlayerDelegateItem(
    val title: String,
    val track: Track
): IDelegateItem

class PlayerCardDelegate: BaseDelegateAdapter<PlayerDelegateItem, PlayerCardDelegate.PlayerCardViewHolder>(
    PlayerDelegateItem::class.java
) {
    class PlayerCardViewHolder(itemView: View): DelegateViewHolder<PlayerDelegateItem>(itemView){
        private val trackCover = itemView.findViewById<ImageView>(R.id.iv_track_cover)
        private val trackTitle = itemView.findViewById<TextView>(R.id.tv_track_title)
        private val trackAuthor = itemView.findViewById<TextView>(R.id.tv_track_author)
        override fun bind(item: PlayerDelegateItem) {
            val track = item.track
            Glide
                .with(itemView.context)
                .load(track.album.cover)
                .into(trackCover)
            trackTitle.text = track.title
            trackAuthor.text = track.album.artists.getNames()
        }
    }

    override fun createViewHolder(binding: View): RecyclerView.ViewHolder {
        return PlayerCardViewHolder(binding)
    }

    override fun getLayoutId(): Int {
        return R.layout.section_player
    }
}