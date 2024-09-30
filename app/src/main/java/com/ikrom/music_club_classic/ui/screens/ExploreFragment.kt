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
import ru.ikrom.ui.base_adapter.delegates.CardAdapter
import ru.ikrom.ui.base_adapter.delegates.NestedItemsDelegate
import ru.ikrom.ui.base_adapter.delegates.NestedItems
import ru.ikrom.ui.base_adapter.delegates.TitleDelegate
import ru.ikrom.ui.base_adapter.delegates.TitleItem
import com.ikrom.music_club_classic.ui.components.AppBar
import com.ikrom.music_club_classic.viewmodel.AlbumViewModel
import com.ikrom.music_club_classic.viewmodel.ExploreViewModel
import dagger.hilt.android.AndroidEntryPoint
import ru.ikrom.ui.base_adapter.CompositeAdapter
import ru.ikrom.ui.base_adapter.item_decorations.MarginItemDecoration

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
                val cardItems = albums
                val newReleasesItem = NestedItems(
                    cardItems,
                    CompositeAdapter.Builder()
                        .add(CardAdapter(
                            onClick = { onAlbumClick(it.id) },
                            onLongClick = {}
                        ))
                        .build())
                adapter.updateItem(0, TitleItem("New releases"))
                adapter.updateItem(1, newReleasesItem)
            }
        }
    }

    private fun onAlbumClick(id: String){
        albumViewModel.updateAlbum(id)
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