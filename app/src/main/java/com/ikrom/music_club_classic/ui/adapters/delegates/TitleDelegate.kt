package com.ikrom.music_club_classic.ui.adapters.delegates

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ikrom.music_club_classic.R
import com.ikrom.music_club_classic.ui.adapters.base_adapters.BaseDelegateAdapter
import com.ikrom.music_club_classic.ui.adapters.base_adapters.IDelegateItem

data class TitleItem(
    val title: String
) : IDelegateItem


class TitleDelegate(): BaseDelegateAdapter<TitleItem, TitleDelegate.TitleDelegateViewHolder>(
    TitleItem::class.java
) {
    inner class TitleDelegateViewHolder(itemView: View): DelegateViewHolder<TitleItem>(itemView){
        private val title: TextView = itemView.findViewById(R.id.tv_title)

        override fun bind(item: TitleItem) {
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