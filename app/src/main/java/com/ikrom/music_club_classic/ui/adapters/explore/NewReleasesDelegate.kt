package com.ikrom.music_club_classic.ui.adapters.explore

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ikrom.music_club_classic.R
import com.ikrom.music_club_classic.data.model.Album
import com.ikrom.music_club_classic.ui.adapters.base_adapters.BaseAdapterCallBack
import com.ikrom.music_club_classic.ui.adapters.base_adapters.BaseDelegateAdapter
import com.ikrom.music_club_classic.ui.adapters.base_adapters.IDelegateItem
import com.ikrom.music_club_classic.ui.adapters.base_adapters.item_decorations.MarginItemDecoration

data class NewReleasesDelegateItem(
    val title: String,
    val albums: List<Album>
): IDelegateItem

class NewReleasesDelegate(
    val onItemClick: (Album) -> Unit
): BaseDelegateAdapter<NewReleasesDelegateItem, NewReleasesDelegate.CardViewHolder>(
    NewReleasesDelegateItem::class.java,){
    inner class CardViewHolder(itemView: View):
        DelegateViewHolder<NewReleasesDelegateItem>(itemView)
    {
        private val title = itemView.findViewById<TextView>(R.id.tv_ection_title)
        private val recyclerView = itemView.findViewById<RecyclerView>(R.id.rv_horizontal_tracks)
        private val adapter = NewReleasesAdapter()

        override fun bind(item: NewReleasesDelegateItem) {
            title.text = item.title
            adapter.setItems(item.albums)
            setupRecycleView()
            setupClicks()
        }

        private fun setupRecycleView(){
            val layout = LinearLayoutManager(itemView.context)
            layout.orientation = LinearLayoutManager.HORIZONTAL
            recyclerView.adapter = adapter
            recyclerView.layoutManager = layout
            if (recyclerView.itemDecorationCount == 0) {
                recyclerView.addItemDecoration(
                    MarginItemDecoration(
                    startSpace = itemView.resources.getDimensionPixelSize(R.dimen.content_horizontal_margin),
                    endSpace =  itemView.resources.getDimensionPixelSize(R.dimen.content_horizontal_margin),
                    betweenSpace = itemView.resources.getDimensionPixelSize(R.dimen.items_margin),
                    isHorizontal = true
                )
                )
            }
        }

        private fun setupClicks(){
            adapter.attachCallBack(object : BaseAdapterCallBack<Album>(){
                override fun onItemClick(item: Album, view: View) {
                    onItemClick(item)
                }
            })
        }
    }

    override fun createViewHolder(binding: View): RecyclerView.ViewHolder {
        return CardViewHolder(binding)
    }

    override fun getLayoutId(): Int {
        return R.layout.section_nested_list
    }
}