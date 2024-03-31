package com.ikrom.music_club_classic.ui.adapters.search

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ikrom.music_club_classic.R
import com.ikrom.music_club_classic.ui.adapters.base_adapters.BaseDelegateAdapter
import com.ikrom.music_club_classic.ui.adapters.base_adapters.IDelegateItem

data class TitleDelegateItem(
    val title: String
) : IDelegateItem


class TitleDelegate(): BaseDelegateAdapter<TitleDelegateItem, TitleDelegate.TitleDelegateViewHolder>(
    TitleDelegateItem::class.java
) {
    inner class TitleDelegateViewHolder(itemView: View): DelegateViewHolder<TitleDelegateItem>(itemView){
        private val title: TextView = itemView.findViewById(R.id.tv_title)

        override fun bind(item: TitleDelegateItem) {
            title.text = item.title
        }

    }

    override fun createViewHolder(binding: View): RecyclerView.ViewHolder {
        return TitleDelegateViewHolder(binding)
    }

    override fun getLayoutId(): Int {
        return R.layout.layout_title
    }
}