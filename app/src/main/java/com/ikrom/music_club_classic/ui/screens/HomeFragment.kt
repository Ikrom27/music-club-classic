package com.ikrom.music_club_classic.ui.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ikrom.music_club_classic.R
import ru.ikrom.ui.base_adapter.delegates.CardAdapter
import ru.ikrom.ui.base_adapter.delegates.NestedItemsDelegate
import ru.ikrom.ui.base_adapter.delegates.NestedItems
import ru.ikrom.ui.base_adapter.delegates.ThumbnailMediumAdapter
import ru.ikrom.ui.base_adapter.delegates.TitleDelegate
import ru.ikrom.ui.base_adapter.delegates.TitleItem
import com.ikrom.music_club_classic.ui.menu.TracksMenu
import com.ikrom.music_club_classic.viewmodel.BottomMenuViewModel
import com.ikrom.music_club_classic.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import ru.ikrom.ui.base_adapter.CompositeAdapter
import ru.ikrom.ui.base_adapter.delegates.QuickPickDelegate
import ru.ikrom.ui.base_adapter.delegates.QuickPickItem
import ru.ikrom.ui.base_adapter.item_decorations.MarginItemDecoration
import ru.ikrom.youtube_data.model.TrackModel

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private lateinit var navController: NavController
    private val homeViewModel: HomeViewModel by activityViewModels()
    private val bottomMenuViewModel: BottomMenuViewModel by activityViewModels()

    private lateinit var compositeAdapter: CompositeAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.rv_main)
        navController = requireParentFragment().findNavController()
        bottomMenuViewModel.navController = navController
        setupAdapter()
        setupRecyclerView(recyclerView)
        setupAdapterData()
        return view
    }

    private fun setupAdapter(){
        compositeAdapter = CompositeAdapter.Builder()
            .add(QuickPickDelegate(
                isPlaying = homeViewModel.isPlaying,
//                currentMediaItem= homeViewModel.currentTrack,
                lifecycleOwner = viewLifecycleOwner,
                onPlayPauseClick = { onPlayPauseClick(it) },
                onSkipClick = {}
            ))
            .add(NestedItemsDelegate())
            .add(TitleDelegate())
            .build()
    }

    private fun setupAdapterData(){
        compositeAdapter.setItems(listOf(
            TitleItem("Quick pick"),
            QuickPickItem(track = null),
            TitleItem(""),
            TitleItem(""),
            TitleItem(""),
            TitleItem("")
        ))
        homeViewModel.quickPick.observe(viewLifecycleOwner) { tracks ->
            if (tracks.isNotEmpty()){
                compositeAdapter.updateItem(1,
                    QuickPickItem(track = tracks[0]))
            }
        }
        homeViewModel.userPlaylists.observe(viewLifecycleOwner) {playlists ->
            if (playlists.isNotEmpty()){
                compositeAdapter.updateItem(2, TitleItem("Your playlists"))
                compositeAdapter.updateItem(3,
                    NestedItems(
                        items = playlists,
                        adapter = CompositeAdapter.Builder()
                            .add(
                                CardAdapter(
                                onClick = {},
                                onLongClick = {})
                            )
                            .build()
                    )
                )
            }
        }
        homeViewModel.trackList.observe(viewLifecycleOwner) {tracks ->
            if (tracks.isNotEmpty()){
                compositeAdapter.updateItem(4, TitleItem("From Linkin park"))
                compositeAdapter.updateItem(5,
                    NestedItems(
                        adapter = CompositeAdapter.Builder()
                            .add(
                                ThumbnailMediumAdapter(
                                onClick = {
                                    homeViewModel.playTrackById(it.id)
                                },
                                onLongClick = {
                                    bottomMenuViewModel.trackLiveData.postValue(homeViewModel.getTrackById(it.id))
                                    val bottomMenu = TracksMenu()
                                    bottomMenu.show(parentFragmentManager, bottomMenu.tag)
                                }
                            )
                        ).build(),
                        items = tracks
                    )
                )
            }
        }
    }

    private fun setupRecyclerView(recyclerView: RecyclerView){
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = compositeAdapter
        val playerHeight = resources.getDimensionPixelSize(R.dimen.mini_player_height)
        val navbarHeight = resources.getDimensionPixelSize(R.dimen.bottom_nav_bar_height)
        val margin = resources.getDimensionPixelSize(R.dimen.section_margin)
        if (recyclerView.itemDecorationCount == 0){
            recyclerView.addItemDecoration(
                MarginItemDecoration(
                    endSpace = playerHeight + navbarHeight + margin,
                )
            )
        }
    }

    private fun onPlayPauseClick(track: TrackModel){
        if (homeViewModel.currentTrack.value!!.mediaId == track.videoId){
            homeViewModel.togglePlayPause()
        }
    }
}