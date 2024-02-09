package com.ikrom.music_club_classic.ui.screens.home

import android.view.View
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ikrom.music_club_classic.R
import com.ikrom.music_club_classic.data.model.Track
import com.ikrom.music_club_classic.ui.base_adapters.BaseAdapterCallBack
import com.ikrom.music_club_classic.ui.base_adapters.BaseDelegateAdapter
import com.ikrom.music_club_classic.ui.base_adapters.IDelegateItem
import com.ikrom.music_club_classic.ui.base_adapters.item_decorations.MarginItemDecoration

data class AuthorTracksDelegateItem(
    val title: String,
    val tracks: LiveData<List<Track>>
): IDelegateItem

class ArtistTracksDelegate(
    val callBack: BaseAdapterCallBack<Track>? = null
): BaseDelegateAdapter<AuthorTracksDelegateItem, ArtistTracksDelegate.TrackViewHolder>(
    AuthorTracksDelegateItem::class.java) {

    override fun createViewHolder(binding: View): RecyclerView.ViewHolder {
        return TrackViewHolder(binding)
    }

    override fun getLayoutId(): Int {
        return R.layout.section_nested_list
    }

    inner class TrackViewHolder(itemView: View): DelegateViewHolder<AuthorTracksDelegateItem>(itemView){
        private val title = itemView.findViewById<TextView>(R.id.section_title)
        private val recyclerView = itemView.findViewById<RecyclerView>(R.id.rv_horizontal_tracks)
        private val adapter = LargeTracksAdapter()

        override fun bind(item: AuthorTracksDelegateItem) {
            title.text = item.title
            item.tracks.observeForever { tracks ->
                adapter.setItems(tracks)
            }
            setupAdapter()
        }

        fun setupAdapter(){
            adapter.attachCallBack(callBack)
            recyclerView.layoutManager = LinearLayoutManager(itemView.context)
                .apply { orientation = LinearLayoutManager.HORIZONTAL}
            recyclerView.adapter = adapter
            if (recyclerView.itemDecorationCount == 0) {
                recyclerView.addItemDecoration(MarginItemDecoration(
                    startSpace = itemView.resources.getDimensionPixelSize(R.dimen.content_horizontal_margin),
                    endSpace =  itemView.resources.getDimensionPixelSize(R.dimen.content_horizontal_margin),
                    betweenSpace = itemView.resources.getDimensionPixelSize(R.dimen.items_margin),
                    isHorizontal = true
                ))
            }
        }
    }
}