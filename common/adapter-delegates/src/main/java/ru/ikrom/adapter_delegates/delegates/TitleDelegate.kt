package ru.ikrom.adapter_delegates.delegates

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.ikrom.adapter_delegates.R
import ru.ikrom.base_adapter.AdapterItem
import ru.ikrom.base_adapter.DelegateAdapter

data class TitleItem(
    val title: String,
): AdapterItem


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