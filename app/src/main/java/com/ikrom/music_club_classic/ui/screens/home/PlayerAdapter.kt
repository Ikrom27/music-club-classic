package com.ikrom.music_club_classic.ui.screens.home

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ikrom.music_club_classic.R
import com.ikrom.music_club_classic.ui.adapters.DelegateAdapter
import com.ikrom.music_club_classic.ui.adapters.DelegateAdapterItem

data class PlayerDelegateItem(
    val title: String,
    val content: String
): DelegateAdapterItem{
    override fun id(): Any {
        return title
    }

    override fun content(): Any {
        return content
    }
}

class PlayerAdapter: DelegateAdapter<PlayerDelegateItem, PlayerAdapter.PlayerViewHolder>(
    PlayerDelegateItem::class.java
) {
    class PlayerViewHolder(itemView: View): BaseViewHolder(itemView){
        val title = itemView.findViewById<TextView>(R.id.title)
        val content = itemView.findViewById<TextView>(R.id.content)
        override fun bind(item: DelegateAdapterItem) {
            val model = item as PlayerDelegateItem
            title.text = model.title
            content.text = model.content
        }
    }

    override fun createViewHolder(binding: View): RecyclerView.ViewHolder {
        return PlayerViewHolder(binding)
    }

    override fun getLayoutId(): Int {
        return R.layout.item_player
    }
}