package com.ikrom.music_club_classic.ui.screens.home

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ikrom.music_club_classic.R
import com.ikrom.music_club_classic.ui.adapters.DelegateAdapter
import com.ikrom.music_club_classic.ui.adapters.DelegateAdapterItem

data class HorizontalTracksDelegateItem(
    val title: String,
    val tracks: List<Int>
): DelegateAdapterItem {
    override fun id(): Any {
        return title
    }

    override fun content(): Any {
        return tracks
    }
}

class TrackAdapter: DelegateAdapter<HorizontalTracksDelegateItem, TrackAdapter.TrackViewHolder>(
    HorizontalTracksDelegateItem::class.java) {

    override fun createViewHolder(binding: View): RecyclerView.ViewHolder {
        return TrackViewHolder(binding)
    }

    override fun getLayoutId(): Int {
        return R.layout.item_track_horizontal
    }

    inner class TrackViewHolder(itemView: View): BaseViewHolder(itemView){
        val title = itemView.findViewById<TextView>(R.id.track_title)
        override fun bind(item: DelegateAdapterItem) {
            title.text = item.id() as String
        }
    }
}