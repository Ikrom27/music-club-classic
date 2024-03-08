package com.ikrom.music_club_classic.ui.adapters.home

import android.view.View
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.navigation.NavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ikrom.music_club_classic.R
import com.ikrom.music_club_classic.data.model.PlayList
import com.ikrom.music_club_classic.ui.adapters.base_adapters.BaseAdapterCallBack
import com.ikrom.music_club_classic.ui.adapters.base_adapters.BaseDelegateAdapter
import com.ikrom.music_club_classic.ui.adapters.base_adapters.IDelegateItem
import com.ikrom.music_club_classic.ui.adapters.base_adapters.item_decorations.MarginItemDecoration

data class CardDelegateItem(
    val title: String,
    val playLists: LiveData<List<PlayList>>
): IDelegateItem

class CardDelegate(
    val navController: NavController
): BaseDelegateAdapter<CardDelegateItem, CardDelegate.CardViewHolder>(
    CardDelegateItem::class.java,){
    inner class CardViewHolder(itemView: View):
        DelegateViewHolder<CardDelegateItem>(itemView)
    {
        private val title = itemView.findViewById<TextView>(R.id.tv_ection_title)
        private val recyclerView = itemView.findViewById<RecyclerView>(R.id.rv_horizontal_tracks)
        private val adapter = CardAdapter()

        override fun bind(item: CardDelegateItem) {
            title.text = item.title
            item.playLists.observeForever { playLists ->
                adapter.setItems(playLists)
            }

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
            adapter.attachCallBack(object : BaseAdapterCallBack<PlayList>(){
                override fun onItemClick(item: PlayList, view: View) {
                    navController.navigate(R.id.action_homeFragment_to_albumFragment)
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