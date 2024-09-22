package com.ikrom.music_club_classic.ui.adapters.delegates

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ikrom.music_club_classic.R
import ru.ikrom.ui.base_adapter.CompositeAdapter
import ru.ikrom.ui.base_adapter.DelegateAdapter
import ru.ikrom.ui.base_adapter.item_decorations.MarginItemDecoration
import ru.ikrom.ui.base_adapter.model.AdapterItem

data class NestedItems<T: AdapterItem>(
    val items: List<T>,
    val adapter: CompositeAdapter,
): AdapterItem()

class NestedItemsDelegate: DelegateAdapter<NestedItems<AdapterItem>, NestedItemsDelegate.RecyclerViewHolder>(
    NestedItems::class.java as Class<out NestedItems<AdapterItem>>){
    inner class RecyclerViewHolder(itemView: View):
        ViewHolder<NestedItems<AdapterItem>>(itemView)
    {
        private val rvHorizontalItems = itemView.findViewById<RecyclerView>(R.id.rv_horizontal_items)
        private val marginStart = itemView.resources.getDimensionPixelSize(R.dimen.content_horizontal_margin)
        private val marginEnd = itemView.resources.getDimensionPixelSize(R.dimen.content_horizontal_margin)
        private val marginBetween = itemView.resources.getDimensionPixelSize(R.dimen.items_margin)

        override fun bind(item: NestedItems<AdapterItem>) {
            item.adapter.setItems(item.items)
            rvHorizontalItems.adapter = item.adapter
            setupRecycleView()
        }

        private fun setupRecycleView(){
            val layout = LinearLayoutManager(itemView.context)
            layout.orientation = LinearLayoutManager.HORIZONTAL
            rvHorizontalItems.layoutManager = layout
            if (rvHorizontalItems.itemDecorationCount == 0) {
                rvHorizontalItems.addItemDecoration(
                    MarginItemDecoration(
                        startSpace = marginStart,
                        endSpace = marginEnd,
                        betweenSpace = marginBetween,
                        isHorizontal = true
                    )
                )
            }
        }
    }

    override fun getViewHolder(binding: View): RecyclerView.ViewHolder {
        return RecyclerViewHolder(binding)
    }

    override fun getLayoutId(): Int {
        return R.layout.item_nested_items
    }
}