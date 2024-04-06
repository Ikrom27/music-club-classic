package com.ikrom.music_club_classic.ui.adapters.delegates

import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ikrom.music_club_classic.R
import com.ikrom.music_club_classic.data.model.Track
import com.ikrom.music_club_classic.extensions.getNames
import com.ikrom.music_club_classic.ui.adapters.base_adapters.BaseDelegateAdapter
import com.ikrom.music_club_classic.ui.adapters.base_adapters.IDelegateItem

data class MediumTrackItem(
    val title: String,
    val subtitle: String,
    val thumbnail: String,
    val onItemClick: () -> Unit,
    val onButtonClick: () -> Unit
) : IDelegateItem


class MediumTrackDelegate:
    BaseDelegateAdapter<MediumTrackItem, MediumTrackDelegate.MediumTrackDelegateViewHolder>(
    MediumTrackItem::class.java
) {
    inner class MediumTrackDelegateViewHolder(itemView: View): DelegateViewHolder<MediumTrackItem>(itemView) {
        private val ivThumbnail: ImageView = itemView.findViewById(R.id.iv_thumbnail)
        private val tvTitle: TextView = itemView.findViewById(R.id.tv_title)
        private val tvSubtitle: TextView = itemView.findViewById(R.id.tv_subtitle)
        private val btnMore: ImageButton = itemView.findViewById(R.id.ib_more)

        override fun bind(item: MediumTrackItem) {
            Glide
                .with(itemView.context)
                .load(item.thumbnail)
                .into(ivThumbnail)
            tvTitle.text = item.title
            tvSubtitle.text= item.subtitle
            itemView.setOnClickListener { item.onItemClick() }
            btnMore.setOnClickListener { item.onButtonClick() }
        }
    }

    override fun createViewHolder(binding: View): RecyclerView.ViewHolder {
        return MediumTrackDelegateViewHolder(binding)
    }

    override fun getLayoutId(): Int {
        return R.layout.layout_medium_covers
    }
}