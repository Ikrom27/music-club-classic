package com.ikrom.music_club_classic.ui.adapters.delegates

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ikrom.music_club_classic.R
import ru.ikrom.ui.BaseDelegateAdapter
import ru.ikrom.ui.model.AdapterItem

data class MenuHeaderDelegateItem(
    val title: String,
    val subtitle: String,
    val thumbnail: String,
    override val onClick: () -> Unit
): AdapterItem()

class MenuHeaderDelegate: BaseDelegateAdapter<MenuHeaderDelegateItem, MenuHeaderDelegate.MenuHeaderViewHolder>(
    MenuHeaderDelegateItem::class.java) {
    inner class MenuHeaderViewHolder(itemView: View): DelegateViewHolder<MenuHeaderDelegateItem>(itemView){
        private val tvTitle: TextView = itemView.findViewById(R.id.tv_title)
        private val tvSubtitle: TextView = itemView.findViewById(R.id.tv_subtitle)
        private val ivThumbnail: ImageView = itemView.findViewById(R.id.iv_thumbnail)

        override fun bind(item: MenuHeaderDelegateItem) {
            tvTitle.text = item.title
            tvSubtitle.text = item.subtitle
            Glide
                .with(itemView.context)
                .load(item.thumbnail)
                .centerCrop()
                .into(ivThumbnail)
        }

    }

    override fun createViewHolder(binding: View): RecyclerView.ViewHolder {
        return MenuHeaderViewHolder(binding)
    }

    override fun getLayoutId(): Int {
        return R.layout.item_menu_header
    }

}