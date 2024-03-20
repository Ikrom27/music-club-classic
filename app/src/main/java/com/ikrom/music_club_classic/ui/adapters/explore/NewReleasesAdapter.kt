package com.ikrom.music_club_classic.ui.adapters.explore

import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.ikrom.music_club_classic.R
import com.ikrom.music_club_classic.data.model.Album
import com.ikrom.music_club_classic.extensions.getNames
import com.ikrom.music_club_classic.ui.adapters.base_adapters.BaseAdapter
import jp.wasabeef.glide.transformations.BlurTransformation

class NewReleasesAdapter: BaseAdapter<Album>() {
    inner class CardViewHolder(itemView: View): BaseViewHolder<Album>(itemView){
        private val cover = itemView.findViewById<ImageView>(R.id.iv_thumbnail)
        private val backgroundImage = itemView.findViewById<ImageView>(R.id.iv_background_image)
        private val title = itemView.findViewById<TextView>(R.id.tv_title)
        private val subtitle = itemView.findViewById<TextView>(R.id.tv_subtitle)
        override fun bind(item: Album) {
            Glide
                .with(itemView.context)
                .load(item.thumbnail)
                .into(cover)
            Glide
                .with(itemView.context)
                .load(item.thumbnail)
                .centerCrop()
                .transform(BlurTransformation(128))
                .into(backgroundImage)
            Log.d("Playlist", "${item.id} ${item.title}")
            title.text = item.title
            subtitle.text = item.artists?.getNames()
        }

    }

    override fun getViewHolder(binding: View): BaseViewHolder<Album> {
        return CardViewHolder(binding)
    }

    override fun getLayoutId(): Int {
        return R.layout.item_new_release_album
    }
}