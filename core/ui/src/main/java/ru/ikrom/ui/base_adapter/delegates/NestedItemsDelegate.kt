package ru.ikrom.ui.base_adapter.delegates

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.ikrom.theme.AppDimens
import ru.ikrom.ui.R
import ru.ikrom.ui.base_adapter.CompositeAdapter
import ru.ikrom.ui.base_adapter.DelegateAdapter
import ru.ikrom.ui.base_adapter.item_decorations.MarginItemDecoration

data class NestedItems<T: Any>(
    val items: List<T>,
    val adapter: CompositeAdapter,
)

class NestedItemsDelegate: DelegateAdapter<NestedItems<*>, NestedItemsDelegate.RecyclerViewHolder>(
    NestedItems::class.java as Class<out NestedItems<*>>){
    inner class RecyclerViewHolder(itemView: View):
        ViewHolder<NestedItems<*>>(itemView)
    {
        private val rvHorizontalItems = itemView.findViewById<RecyclerView>(R.id.rv_horizontal_items)
        private val marginStart = itemView.resources.getDimensionPixelSize(AppDimens.MARGIN_CONTENT_HORIZONTAL)
        private val marginEnd = itemView.resources.getDimensionPixelSize(AppDimens.MARGIN_CONTENT_HORIZONTAL)
        private val marginBetween = itemView.resources.getDimensionPixelSize(AppDimens.MARGIN_ITEMS)

        override fun bind(item: NestedItems<*>) {
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