package com.ikrom.music_club_classic.ui.adapters.bottom_menu

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ikrom.music_club_classic.R
import com.ikrom.music_club_classic.ui.adapters.base_adapters.BaseDelegateAdapter
import com.ikrom.music_club_classic.ui.adapters.base_adapters.IDelegateItem

data class MenuButtonItem(
    val title: String,
    val icon: Int,
    val onClick: () -> Unit
) : IDelegateItem

class MenuButtonDelegate: BaseDelegateAdapter<MenuButtonItem, MenuButtonDelegate.MenuButtonViewHolder>(
    MenuButtonItem::class.java
) {
    inner class MenuButtonViewHolder(itemView: View) : DelegateViewHolder<MenuButtonItem>(itemView){
        private val container: View = itemView.findViewById(R.id.container)
        private val ivIcon: ImageView = itemView.findViewById(R.id.iv_icon)
        private val tvTitle: TextView = itemView.findViewById(R.id.tv_title)

        override fun bind(item: MenuButtonItem) {
            container.setOnClickListener { item.onClick() }
            ivIcon.setImageResource(item.icon)
            tvTitle.text = item.title
        }

    }

    override fun createViewHolder(binding: View): RecyclerView.ViewHolder {
        return MenuButtonViewHolder(binding)
    }

    override fun getLayoutId(): Int {
        return R.layout.item_menu_button
    }
}