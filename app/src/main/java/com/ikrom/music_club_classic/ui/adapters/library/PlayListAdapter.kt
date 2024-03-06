package com.ikrom.music_club_classic.ui.adapters.library

import android.view.View
import android.view.View.OnClickListener
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.ikrom.innertube.models.PlaylistItem
import com.ikrom.music_club_classic.R
import com.ikrom.music_club_classic.data.model.PlayList
import com.ikrom.music_club_classic.ui.adapters.base_adapters.BaseAdapter

class PlayListAdapter(
    val onButtonClickListener: () -> Unit
): BaseAdapter<PlayList>() {
    override fun getViewHolder(binding: View): BaseViewHolder<PlayList> {
        return PlayListViewHolder(binding)
    }

    override fun getLayoutId(): Int {
        return R.layout.item_library_playlist
    }

    inner class PlayListViewHolder(itemView: View): BaseViewHolder<PlayList>(itemView){
        private val coverView: ImageView = itemView.findViewById(R.id.iv_playlist_cover)
        private val titleView: TextView = itemView.findViewById(R.id.tv_title)
        private val subTitleView: TextView = itemView.findViewById(R.id.tv_subtitle)
        private val button: ImageButton = itemView.findViewById(R.id.ib_more)

        override fun bind(item: PlayList) {
            Glide
                .with(itemView.context)
                .load(item.thumbnail)
                .into(coverView)
            titleView.text = item.title
            subTitleView.text = item.author?.name
            button.setOnClickListener{ onButtonClickListener() }
        }

    }
}