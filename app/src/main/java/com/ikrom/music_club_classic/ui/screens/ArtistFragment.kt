package com.ikrom.music_club_classic.ui.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ikrom.base_adapter.CompositeAdapter
import com.ikrom.base_adapter.item_decorations.MarginItemDecoration
import com.ikrom.music_club_classic.R
import com.ikrom.music_club_classic.extensions.models.toThumbnailSmallItem
import com.ikrom.music_club_classic.ui.adapters.delegates.ThumbnailSmallDelegate
import com.ikrom.music_club_classic.ui.adapters.delegates.NestedItemsDelegate
import com.ikrom.music_club_classic.ui.adapters.delegates.ArtistHeaderDelegate
import com.ikrom.music_club_classic.ui.adapters.delegates.ArtistHeaderItem
import com.ikrom.music_club_classic.ui.adapters.delegates.TitleDelegate
import com.ikrom.music_club_classic.ui.adapters.delegates.TitleItem
import com.ikrom.music_club_classic.viewmodel.ArtistViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ArtistFragment : Fragment() {
    private val viewModel: ArtistViewModel by activityViewModels()

    private lateinit var recyclerView: RecyclerView

    private val compositeAdapter = CompositeAdapter.Builder()
        .add(TitleDelegate())
        .add(ArtistHeaderDelegate())
        .add(NestedItemsDelegate())
        .add(ThumbnailSmallDelegate())
        .build()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_artist, container, false)
        bindView(view)
        setupRecyclerView()
        setupAdapterData()
        return view
    }

    private fun setupAdapterData(){
        viewModel.artistLiveData.observe(viewLifecycleOwner) {
            if (it != null){
                val tracks =  it.tracks?.map { it.toThumbnailSmallItem({}, {}) } ?: emptyList()
                compositeAdapter.addToEnd(ArtistHeaderItem(
                    title = it.title,
                    subtitle = "",
                    thumbnail = it.thumbnail,
                    onPlayClick = {},
                    onShuffleClick = {}
                ))
                compositeAdapter.addToEnd(TitleItem("Tracks"))
                compositeAdapter.addItems(tracks)
            }

        }
    }

    private fun bindView(view: View) {
        recyclerView = view.findViewById(R.id.rv_content)
    }

    private fun setupRecyclerView(){
        recyclerView.adapter = compositeAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val playerHeight = resources.getDimensionPixelSize(R.dimen.mini_player_height)
        val navbarHeight = resources.getDimensionPixelSize(R.dimen.bottom_nav_bar_height)
        if (recyclerView.itemDecorationCount == 0){
            recyclerView.addItemDecoration(
                MarginItemDecoration(
                    endSpace = playerHeight + navbarHeight
                )
            )
        }
    }
}