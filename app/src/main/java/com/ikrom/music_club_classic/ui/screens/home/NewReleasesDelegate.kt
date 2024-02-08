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
        val title = itemView.findViewById<TextView>(R.id.section_title)
        val recyclerView = itemView.findViewById<RecyclerView>(R.id.rv_horizontal_tracks)
        val adapter = NewReleasesAdapter()
        val layout = LinearLayoutManager(itemView.context)

        override fun bind(item: IDelegateAdapterItem) {
            title.text = item.id() as String
            layout.orientation = LinearLayoutManager.HORIZONTAL
            recyclerView.layoutManager = layout
            (item.content() as LiveData<List<Album>>).observeForever { albums ->
                adapter.setItems(albums)
            }
            Log.d("NewReleasesViewHolder", "binding")
            recyclerView.adapter = adapter
        }
    }

    override fun createViewHolder(binding: View): RecyclerView.ViewHolder {
        return NewReleasesViewHolder(binding)
    }

    override fun getLayoutId(): Int {
        return R.layout.section_nested_list
    }
}