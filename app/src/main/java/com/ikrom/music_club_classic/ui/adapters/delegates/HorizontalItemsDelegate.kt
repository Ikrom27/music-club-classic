package com.ikrom.music_club_classic.ui.adapters.delegates

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ikrom.music_club_classic.R
import com.ikrom.base_adapter.model.AdapterItem

data class HorizontalItems<T: AdapterItem>(
    val title: String,
    val adapter: com.ikrom.base_adapter.BaseAdapter<T>,
    val items: List<T>,
): AdapterItem()

class HorizontalItemsDelegate: com.ikrom.base_adapter.BaseDelegateAdapter<HorizontalItems<AdapterItem>, HorizontalItemsDelegate.RecyclerViewHolder>(
    HorizontalItems::class.java as Class<out HorizontalItems<AdapterItem>>){
    inner class RecyclerViewHolder(itemView: View):
        DelegateViewHolder<HorizontalItems<AdapterItem>>(itemView)
    {
        private val title = itemView.findViewById<TextView>(R.id.tv_ection_title)
        private val recyclerView = itemView.findViewById<RecyclerView>(R.id.rv_horizontal_tracks)
        private val marginStart = itemView.resources.getDimensionPixelSize(R.dimen.content_horizontal_margin)
        private val marginEnd = itemView.resources.getDimensionPixelSize(R.dimen.content_horizontal_margin)
        private val marginBetween = itemView.resources.getDimensionPixelSize(R.dimen.items_margin)

        override fun bind(item: HorizontalItems<AdapterItem>) {
            title.text = item.title
            item.adapter.setItems(item.items)
            recyclerView.adapter = item.adapter
            setupRecycleView()
        }

        private fun setupRecycleView(){
            val layout = LinearLayoutManager(itemView.context)
            layout.orientation = LinearLayoutManager.HORIZONTAL
            recyclerView.layoutManager = layout
            if (recyclerView.itemDecorationCount == 0) {
                recyclerView.addItemDecoration(
                    com.ikrom.base_adapter.item_decorations.MarginItemDecoration(
                        startSpace = marginStart,
                        endSpace = marginEnd,
                        betweenSpace = marginBetween,
                        isHorizontal = true
                    )
                )
            }
        }
    }

    override fun createViewHolder(binding: View): RecyclerView.ViewHolder {
        return RecyclerViewHolder(binding)
    }

    override fun getLayoutId(): Int {
        return R.layout.section_nested_list
    }
}