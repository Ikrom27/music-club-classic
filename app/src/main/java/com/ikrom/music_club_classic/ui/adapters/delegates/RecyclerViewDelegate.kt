package com.ikrom.music_club_classic.ui.adapters.delegates

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ikrom.music_club_classic.R
import com.ikrom.music_club_classic.ui.adapters.base_adapters.BaseAdapter
import com.ikrom.music_club_classic.ui.adapters.base_adapters.BaseDelegateAdapter
import com.ikrom.music_club_classic.ui.adapters.base_adapters.IDelegateItem
import com.ikrom.music_club_classic.ui.adapters.base_adapters.item_decorations.MarginItemDecoration

data class RecyclerViewItem(
    val title: String,
    val items: List<IDelegateItem>
): IDelegateItem

class RecyclerViewDelegate<T: IDelegateItem>(
    val nestedAdapter: BaseAdapter<T>
): BaseDelegateAdapter<RecyclerViewItem, RecyclerViewDelegate<T>.RecyclerViewHolder>(
    RecyclerViewItem::class.java,){
    inner class RecyclerViewHolder(itemView: View):
        DelegateViewHolder<RecyclerViewItem>(itemView)
    {
        private val title = itemView.findViewById<TextView>(R.id.tv_ection_title)
        private val recyclerView = itemView.findViewById<RecyclerView>(R.id.rv_horizontal_tracks)
        private val marginStart = itemView.resources.getDimensionPixelSize(R.dimen.content_horizontal_margin)
        private val marginEnd = itemView.resources.getDimensionPixelSize(R.dimen.content_horizontal_margin)
        private val marginBetween = itemView.resources.getDimensionPixelSize(R.dimen.items_margin)

        override fun bind(item: RecyclerViewItem) {
            title.text = item.title
            nestedAdapter.setItems(item.items.map { it as T })
            setupRecycleView()
        }

        private fun setupRecycleView(){
            val layout = LinearLayoutManager(itemView.context)
            layout.orientation = LinearLayoutManager.HORIZONTAL
            recyclerView.adapter = nestedAdapter
            recyclerView.layoutManager = layout
            if (recyclerView.itemDecorationCount == 0) {
                recyclerView.addItemDecoration(
                    MarginItemDecoration(
                    startSpace = marginStart,
                    endSpace =  marginEnd,
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