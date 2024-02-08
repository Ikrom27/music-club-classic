package com.ikrom.music_club_classic.ui.screens.home

import android.view.View
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ikrom.music_club_classic.R
import com.ikrom.music_club_classic.data.model.Track
import com.ikrom.music_club_classic.ui.base_adapters.DelegateAdapter
import com.ikrom.music_club_classic.ui.base_adapters.IDelegateAdapterItem
import com.ikrom.music_club_classic.ui.base_adapters.item_decorations.MarginItemDecoration

data class AuthorTracksDelegateItem(
    val title: String,
    val tracks: LiveData<List<Track>>
): IDelegateAdapterItem {
    override fun id(): Any {
        return title
    }

    override fun content(): Any {
        return tracks
    }
}

class AuthorTracksDelegate: DelegateAdapter<AuthorTracksDelegateItem, AuthorTracksDelegate.TrackViewHolder>(
    AuthorTracksDelegateItem::class.java) {

    override fun createViewHolder(binding: View): RecyclerView.ViewHolder {
        return TrackViewHolder(binding)
    }

    override fun getLayoutId(): Int {
        return R.layout.section_nested_list
    }

    inner class TrackViewHolder(itemView: View): DelegateViewHolder(itemView){
        val title = itemView.findViewById<TextView>(R.id.section_title)
        val recyclerView = itemView.findViewById<RecyclerView>(R.id.rv_horizontal_tracks)
        val adapter = LargeTracksAdapter()

        override fun bind(item: IDelegateAdapterItem) {
            title.text = item.id() as String
            (item.content() as LiveData<List<Track>>).observeForever { tracks ->
                adapter.setItems(tracks)
            }
            setupRecycleView()
        }

        fun setupRecycleView(){
            val layout = LinearLayoutManager(itemView.context)
            layout.orientation = LinearLayoutManager.HORIZONTAL
            recyclerView.layoutManager = layout
            recyclerView.adapter = adapter
            recyclerView.addItemDecoration(MarginItemDecoration(
                startSpace = itemView.resources.getDimensionPixelSize(R.dimen.content_horizontal_margin),
                endSpace =  itemView.resources.getDimensionPixelSize(R.dimen.content_horizontal_margin),
                betweenSpace = itemView.resources.getDimensionPixelSize(R.dimen.items_margin),
                isHorizontal = true
            ))
            recyclerView.invalidateItemDecorations()
        }
    }
}