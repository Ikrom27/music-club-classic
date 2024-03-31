package com.ikrom.music_club_classic.ui.adapters.search

import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.ikrom.music_club_classic.R
import com.ikrom.music_club_classic.data.model.Track
import com.ikrom.music_club_classic.extensions.getNames
import com.ikrom.music_club_classic.ui.adapters.base_adapters.BaseAdapter

class SearchAdapter(
    val onItemClick: (Track) -> Unit,
    val onMoreButtonClick: (Track) -> Unit
): BaseAdapter<Track>() {
    inner class SearchViewHolder(itemView: View): BaseViewHolder<Track>(itemView) {
        val cover: ImageView = itemView.findViewById(R.id.iv_thumbnail)
        val titleTextView: TextView = itemView.findViewById(R.id.tv_title)
        val subtitleTextView: TextView = itemView.findViewById(R.id.tv_subtitle)
//        val downloadIcon: ImageView = itemView.findViewById(R.id.ic_downloaded)
        val moreButton: ImageButton = itemView.findViewById(R.id.ib_more)

        override fun bind(item: Track) {
            Glide
                .with(itemView.context)
                .load(item.album.thumbnail)
                .into(cover)
            titleTextView.text = item.title
            subtitleTextView.text= item.album.artists.getNames()
            moreButton.setImageResource(R.drawable.ic_more_vertical)
            moreButton.setOnClickListener {
                onMoreButtonClick(item)
                Log.d("click", "boob")
            }
        }
    }

    override fun getViewHolder(binding: View): BaseViewHolder<Track> {
        return SearchViewHolder(binding)
    }

    override fun getLayoutId(): Int {
        return R.layout.layout_medium_covers
    }
}