package com.ikrom.music_club_classic.ui.adapters.delegates

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ikrom.music_club_classic.R
import ru.ikrom.ui.base_adapter.DelegateAdapter
import ru.ikrom.ui.base_adapter.model.AdapterItem

data class TitleItem(
    val title: String,
) : AdapterItem()


class TitleDelegate: DelegateAdapter<TitleItem, TitleDelegate.TitleViewHolder>(
    TitleItem::class.java
) {
    inner class TitleViewHolder(itemView: View): ViewHolder<TitleItem>(itemView){
        private val tvTitle: TextView = itemView.findViewById(R.id.tv_title)

        override fun bind(item: TitleItem) {
            tvTitle.text = item.title
        }

    }

    override fun getViewHolder(binding: View): RecyclerView.ViewHolder {
        return TitleViewHolder(binding)
    }

    override fun getLayoutId(): Int {
        return R.layout.item_title
    }
}