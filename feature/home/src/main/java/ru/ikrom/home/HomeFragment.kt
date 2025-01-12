package ru.ikrom.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import ru.ikrom.ui.base_adapter.CompositeAdapter
import ru.ikrom.ui.base_adapter.delegates.CardAdapter
import ru.ikrom.ui.base_adapter.delegates.CardItem
import ru.ikrom.ui.base_adapter.delegates.NestedItems
import ru.ikrom.ui.base_adapter.delegates.NestedItemsDelegate
import ru.ikrom.ui.base_adapter.delegates.ThumbnailItem
import ru.ikrom.ui.base_adapter.delegates.ThumbnailItemMediumItem
import ru.ikrom.ui.base_adapter.delegates.ThumbnailMediumAdapter
import ru.ikrom.ui.base_adapter.delegates.TitleDelegate
import ru.ikrom.ui.base_adapter.delegates.TitleItem
import ru.ikrom.ui.base_adapter.item_decorations.DecorationDimens
import ru.ikrom.ui.base_adapter.item_decorations.MarginItemDecoration
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {
    @Inject
    lateinit var navigator: Navigator
    private lateinit var navController: NavController
    private val homeViewModel: HomeViewModel by viewModels()
    private lateinit var compositeAdapter: CompositeAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.rv_main)
        navController = requireParentFragment().findNavController()
        setupAdapter()
        setupRecyclerView(recyclerView)
        loadItems()
        return view
    }

    private fun setupAdapter(){
        compositeAdapter = CompositeAdapter.Builder()
            .add(NestedItemsDelegate())
            .add(TitleDelegate())
            .build()
    }

    private fun setupRecyclerView(recyclerView: RecyclerView){
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = compositeAdapter
        if (recyclerView.itemDecorationCount == 0){
            recyclerView.addItemDecoration(
                MarginItemDecoration(
                    endSpace = DecorationDimens.getBottomMargin(resources),
                )
            )
        }
    }

    private fun loadItems(){
        homeViewModel.state.observe(viewLifecycleOwner) { state ->
            when(state){
                is UiState.Success -> showContent(state)
                else -> {}
            }
        }
    }

    private fun showContent(data: UiState.Success){
        compositeAdapter.setItems(emptyList())
        if(data.quickPickTracks.isNotEmpty()){
            compositeAdapter.addToEnd(TitleItem("Quick pick"))
            compositeAdapter.addToEnd(createMediumNestedItem(data.quickPickTracks))
        }
        if(data.favoriteTracks.isNotEmpty()){
            compositeAdapter.addToEnd(TitleItem("Favorite"))
            compositeAdapter.addToEnd(createMediumNestedItem(data.favoriteTracks))
        }
        if(data.playlists.isNotEmpty()){
            compositeAdapter.addToEnd(TitleItem("Playlists"))
            compositeAdapter.addToEnd(createCardNestedItem(data.playlists))
        }
    }

    private fun createMediumNestedItem(items: List<ThumbnailItemMediumItem>) = NestedItems(
        adapter = CompositeAdapter.Builder()
            .add(
                ThumbnailMediumAdapter(
                    onClick = {
                        homeViewModel.playTrackById(it.id)
                    },
                    onLongClick = {
                        navigator.toTrackMenu(it)
                    }
                )
            ).build(),
        items = items
    )

    private fun createCardNestedItem(items: List<CardItem>) = NestedItems(
        adapter = CompositeAdapter.Builder()
            .add(
                CardAdapter(
                    onClick = {
                        navigator.toPlaylist(it.id)
                    },
                    onLongClick = {
                        navigator.toPlaylistMenu(it)
                    }
                )
            ).build(),
        items = items
    )

    interface Navigator {
        fun toTrackMenu(item: ThumbnailItem)
        fun toPlaylistMenu(item: ThumbnailItem)
        fun toPlaylist(id: String)
    }
}