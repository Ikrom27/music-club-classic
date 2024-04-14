package com.ikrom.music_club_classic.ui.adapters.bottom_menu

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ikrom.base_adapter.model.AdapterItem
import com.ikrom.music_club_classic.R
import com.ikrom.music_club_classic.data.model.Track
import com.ikrom.music_club_classic.extensions.getNames

data class MenuHeaderDelegateItem(
    val track: Track
): AdapterItem()

class MenuHeaderDelegate(
    private val onClickListener: (Track) -> Unit
): com.ikrom.base_adapter.BaseDelegateAdapter<MenuHeaderDelegateItem, MenuHeaderDelegate.MenuHeaderViewHolder>(MenuHeaderDelegateItem::class.java) {
    inner class MenuHeaderViewHolder(itemView: View): DelegateViewHolder<MenuHeaderDelegateItem>(itemView){
        private val tvTitle: TextView = itemView.findViewById(R.id.tv_title)
        private val tvSubtitle: TextView = itemView.findViewById(R.id.tv_subtitle)
        private val ivThumbnail: ImageView = itemView.findViewById(R.id.iv_thumbnail)

        override fun bind(item: MenuHeaderDelegateItem) {
            tvTitle.text = item.track.title
            tvSubtitle.text = item.track.album.artists.getNames()
            Glide
                .with(itemView.context)
                .load(item.track.album.thumbnail)
                .centerCrop()
                .into(ivThumbnail)
            tvSubtitle.setOnClickListener {
                onClickListener(item.track)
            }
        }

    }

    override fun createViewHolder(binding: View): RecyclerView.ViewHolder {
        return MenuHeaderViewHolder(binding)
    }

    override fun getLayoutId(): Int {
        return R.layout.item_menu_header
    }

}