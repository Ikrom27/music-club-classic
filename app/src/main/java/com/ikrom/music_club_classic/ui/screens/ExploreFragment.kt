package com.ikrom.music_club_classic.ui.screens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ikrom.music_club_classic.R
import com.ikrom.music_club_classic.data.model.Album
import com.ikrom.music_club_classic.extensions.models.albumCardItems
import com.ikrom.base_adapter.CompositeAdapter
import com.ikrom.base_adapter.item_decorations.MarginItemDecoration
import com.ikrom.music_club_classic.ui.adapters.delegates.CardAdapter
import com.ikrom.music_club_classic.ui.adapters.delegates.NestedItemsDelegate
import com.ikrom.music_club_classic.ui.adapters.delegates.NestedItems
import com.ikrom.music_club_classic.ui.adapters.delegates.TitleDelegate
import com.ikrom.music_club_classic.ui.adapters.delegates.TitleItem
import com.ikrom.music_club_classic.ui.components.AppBar
import com.ikrom.music_club_classic.viewmodel.AlbumViewModel
import com.ikrom.music_club_classic.viewmodel.ExploreViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ExploreFragment : Fragment() {
    private val viewModel: ExploreViewModel by viewModels()
    private val albumViewModel: AlbumViewModel by activityViewModels()

    private lateinit var appBar: AppBar
    private lateinit var navController: NavController

    private lateinit var recyclerView: RecyclerView

    private val adapter = CompositeAdapter.Builder()
        .add(NestedItemsDelegate())
        .add(TitleDelegate())
        .build()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_explore, container, false)
        navController = requireParentFragment().findNavController()
        bindViews(view)
        setupButtons(view)
        setupRecyclerView()
        setupAdapterData()
        return view
    }

    private fun setupAdapterData(){
        viewModel.newReleasesList.observe(viewLifecycleOwner){albums ->
            if(albums.isNotEmpty()){
                val cardItems = albums.albumCardItems { onAlbumClick(it) }
                val newReleasesItem = NestedItems(cardItems, CardAdapter())
                adapter.updateItem(0, TitleItem("New releases"))
                adapter.updateItem(1, newReleasesItem)
            }
        }
    }

    private fun onAlbumClick(album: Album){
        albumViewModel.setAlbum(album)
        navController.navigate(R.id.explore_to_album)
    }

    private fun setupRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
        val playerHeight = resources.getDimensionPixelSize(R.dimen.mini_player_height)
        val navbarHeight = resources.getDimensionPixelSize(R.dimen.bottom_nav_bar_height)
        val margin = resources.getDimensionPixelSize(R.dimen.section_margin)
        if (recyclerView.itemDecorationCount == 0){
            recyclerView.addItemDecoration(
                MarginItemDecoration(
                    endSpace = playerHeight + navbarHeight + margin,
                    betweenSpace = margin
                )
            )
        }
    }

    private fun setupButtons(view: View) {
        appBar.setOnSearchClick {
            navController.navigate(R.id.explore_to_search)
        }
    }

    fun bindViews(view: View){
        appBar = view.findViewById(R.id.action_bar)
        recyclerView = view.findViewById(R.id.rv_content)
    }
}