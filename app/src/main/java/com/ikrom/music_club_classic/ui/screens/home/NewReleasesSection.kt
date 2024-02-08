package com.ikrom.music_club_classic.ui.screens.home

import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ikrom.music_club_classic.R
import com.ikrom.music_club_classic.data.model.Album
import com.ikrom.music_club_classic.ui.base_adapters.DelegateAdapter
import com.ikrom.music_club_classic.ui.base_adapters.IDelegateAdapterItem
import com.ikrom.music_club_classic.ui.base_adapters.item_decorations.MarginItemDecoration

data class NewReleasesDelegateItem(
    val title: String,
    val albums: LiveData<List<Album>>
): IDelegateAdapterItem{
    override fun id(): Any {
        return title
    }

    override fun content(): Any {
        return albums
    }

}

class NewReleasesDelegate: DelegateAdapter<NewReleasesDelegateItem, NewReleasesDelegate.NewReleasesViewHolder>(
    NewReleasesDelegateItem::class.java
){
    inner class NewReleasesViewHolder(itemView: View):
        DelegateViewHolder(itemView)
    {
        private val title = itemView.findViewById<TextView>(R.id.section_title)
        private val recyclerView = itemView.findViewById<RecyclerView>(R.id.rv_horizontal_tracks)
        private val adapter = NewReleasesAdapter()

        override fun bind(item: IDelegateAdapterItem) {
            title.text = item.id() as String
            (item.content() as LiveData<List<Album>>).observeForever { albums ->
                adapter.setItems(albums)
            }
            setupRecycleView()
        }

        private fun setupRecycleView(){
            val layout = LinearLayoutManager(itemView.context)
            layout.orientation = LinearLayoutManager.HORIZONTAL
            recyclerView.adapter = adapter
            recyclerView.layoutManager = layout
            recyclerView.addItemDecoration(
                MarginItemDecoration(
                    startSpace = itemView.resources.getDimensionPixelSize(R.dimen.content_horizontal_margin),
                    endSpace = itemView.resources.getDimensionPixelSize(R.dimen.content_horizontal_margin),
                    betweenSpace = itemView.resources.getDimensionPixelSize(R.dimen.items_margin),
                    isHorizontal = true
                )
            )
        }
    }

    override fun createViewHolder(binding: View): RecyclerView.ViewHolder {
        return NewReleasesViewHolder(binding)
    }

    override fun getLayoutId(): Int {
        return R.layout.section_nested_list
    }
}