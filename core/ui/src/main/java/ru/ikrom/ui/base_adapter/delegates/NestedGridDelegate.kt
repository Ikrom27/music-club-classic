package ru.ikrom.ui.base_adapter.delegates

import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.ikrom.theme.AppDimens
import ru.ikrom.ui.R
import ru.ikrom.ui.base_adapter.CompositeAdapter
import ru.ikrom.ui.base_adapter.DelegateAdapter
import ru.ikrom.ui.base_adapter.item_decorations.GridSpacingItemDecoration

data class NestedGridItems<T: Any>(
    val items: List<T>,
    val adapter: CompositeAdapter,
)

class NestedGridDelegate: DelegateAdapter<NestedGridItems<*>, NestedGridDelegate.RecyclerViewHolder>(
    NestedGridItems::class.java as Class<out NestedGridItems<*>>){
    inner class RecyclerViewHolder(itemView: View):
        ViewHolder<NestedGridItems<*>>(itemView)
    {
        private val rvHorizontalItems = itemView.findViewById<RecyclerView>(R.id.rv_horizontal_items)
        private val marginBetween = itemView.resources.getDimensionPixelSize(AppDimens.MARGIN_CONTENT_HORIZONTAL)

        override fun bind(item: NestedGridItems<*>) {
            item.adapter.setItems(item.items)
            rvHorizontalItems.adapter = item.adapter
            setupRecycleView()
        }

        private fun setupRecycleView(){
            val layout = GridLayoutManager(itemView.context, 3, GridLayoutManager.HORIZONTAL, false)
            rvHorizontalItems.layoutManager = layout
            if (rvHorizontalItems.itemDecorationCount == 0) {
                rvHorizontalItems.addItemDecoration(
                    GridSpacingItemDecoration(
                        verticalSpacing = 0,
                        horizontalSpacing = marginBetween
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