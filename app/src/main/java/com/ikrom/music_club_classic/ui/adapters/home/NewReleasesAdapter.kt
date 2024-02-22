package com.ikrom.music_club_classic.ui.adapters.home

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
    inner class NewReleasesViewHolder(itemView: View): BaseViewHolder<Album>(itemView){
        private val albumCover = itemView.findViewById<ImageView>(R.id.iv_album_cover)
        private val backgroundImage = itemView.findViewById<ImageView>(R.id.iv_background_image)
        private val albumTitle = itemView.findViewById<TextView>(R.id.tv_album_title)
        private val albumAuthor = itemView.findViewById<TextView>(R.id.tv_album_author)
        override fun bind(item: Album) {
            Glide
                .with(itemView.context)
                .load(item.cover)
                .into(albumCover)
            Glide
                .with(itemView.context)
                .load(item.cover)
                .centerCrop()
                .transform(BlurTransformation(128))
                .into(backgroundImage)
            albumTitle.text = item.title
            albumAuthor.text = item.artists.getNames()
        }

    }

    override fun getViewHolder(binding: View): BaseViewHolder<Album> {
        return NewReleasesViewHolder(binding)
    }

    override fun getLayoutId(): Int {
        return R.layout.item_new_release_album
    }
}