package com.ikrom.music_club_classic.ui.adapters.player

import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ikrom.music_club_classic.R
import com.ikrom.music_club_classic.ui.adapters.base_adapters.BaseDelegateAdapter
import com.ikrom.music_club_classic.ui.adapters.base_adapters.IDelegateItem
import es.claucookie.miniequalizerlibrary.EqualizerView

data class PlayerQueueItem(
    val title: String,
    val subtitle: String,
    val thumbnail: String,
    val isPlaying: Boolean = false
): IDelegateItem

class PlayerQueueDelegate(
): BaseDelegateAdapter<PlayerQueueItem, PlayerQueueDelegate.PlayerQueueViewHolder>(
    PlayerQueueItem::class.java){
    inner class PlayerQueueViewHolder(itemView: View): DelegateViewHolder<PlayerQueueItem>(itemView){
        val cover: ImageView = itemView.findViewById(R.id.iv_thumbnail)
        val titleTextView: TextView = itemView.findViewById(R.id.tv_title)
        val subtitleTextView: TextView = itemView.findViewById(R.id.tv_subtitle)
        val equalizerView: EqualizerView = itemView.findViewById(R.id.equalizer_view)
        val equalizerShadow: View = itemView.findViewById(R.id.equalizer_view_shadow)

        override fun bind(item: PlayerQueueItem) {
            Glide
                .with(itemView.context)
                .load(item.thumbnail)
                .into(cover)
            titleTextView.text = item.title
            subtitleTextView.text= item.subtitle
            if (item.isPlaying){
                equalizerView.visibility = View.VISIBLE
                equalizerShadow.visibility = View.VISIBLE
                equalizerView.animateBars()
            }
        }

    }

    override fun createViewHolder(binding: View): RecyclerView.ViewHolder {
        return PlayerQueueViewHolder(binding)
    }

    override fun getLayoutId(): Int {
        return R.layout.item_player_queue
    }
}