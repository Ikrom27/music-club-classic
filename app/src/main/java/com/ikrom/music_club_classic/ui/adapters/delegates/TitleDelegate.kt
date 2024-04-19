package com.ikrom.music_club_classic.ui.adapters.delegates

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ikrom.base_adapter.BaseDelegateAdapter
import com.ikrom.music_club_classic.R
import com.ikrom.base_adapter.model.AdapterItem

data class TitleItem(
    val title: String,
) : AdapterItem()


class TitleDelegate: BaseDelegateAdapter<TitleItem, TitleDelegate.TitleDelegateViewHolder>(
    TitleItem::class.java
) {
    inner class TitleDelegateViewHolder(itemView: View): DelegateViewHolder<TitleItem>(itemView){
        private val tvTitle: TextView = itemView.findViewById(R.id.tv_title)

        override fun bind(item: TitleItem) {
            tvTitle.text = item.title
        }

    }

    override fun createViewHolder(binding: View): RecyclerView.ViewHolder {
        return TitleDelegateViewHolder(binding)
    }

    override fun getLayoutId(): Int {
        return R.layout.item_title
    }
}