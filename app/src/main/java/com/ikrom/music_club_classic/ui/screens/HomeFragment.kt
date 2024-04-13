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
import com.ikrom.music_club_classic.data.model.PlayList
import com.ikrom.music_club_classic.data.model.Track
import com.ikrom.music_club_classic.extensions.playlistCardItems
import com.ikrom.music_club_classic.extensions.toLargeThumbnailItems
import com.ikrom.music_club_classic.playback.PlayerHandler
import com.ikrom.music_club_classic.ui.adapters.base_adapters.CompositeAdapter
import com.ikrom.music_club_classic.ui.adapters.base_adapters.item_decorations.MarginItemDecoration
import com.ikrom.music_club_classic.ui.adapters.delegates.CardAdapter
import com.ikrom.music_club_classic.ui.adapters.delegates.HorizontalItemsDelegate
import com.ikrom.music_club_classic.ui.adapters.delegates.HorizontalItems
import com.ikrom.music_club_classic.ui.adapters.delegates.LargeTracksAdapter
import com.ikrom.music_club_classic.ui.adapters.home.QuickPickDelegate
import com.ikrom.music_club_classic.ui.adapters.home.QuickPickItem
import com.ikrom.music_club_classic.viewmodel.BottomMenuViewModel
import com.ikrom.music_club_classic.viewmodel.HomeViewModel
import com.ikrom.music_club_classic.viewmodel.PlayListViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {
    @Inject
    lateinit var playerHandler: PlayerHandler
    private lateinit var navController: NavController

    private val homeViewModel: HomeViewModel by activityViewModels()
    private val playListViewModel: PlayListViewModel by activityViewModels()
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
        setupAdapter()
        setupRecyclerView(recyclerView)
        setupAdapterData()
        return view
    }

    private fun setupAdapter(){
        compositeAdapter = CompositeAdapter.Builder()
            .add(QuickPickDelegate(
                isPlaying = playerHandler.isPlayingLiveData,
                currentMediaItem= playerHandler.currentMediaItemLiveData,
                lifecycleOwner = viewLifecycleOwner,
                onPlayPauseClick = { onPlayPauseClick(it) },
                onSkipClick = {}
            ))
            .add(HorizontalItemsDelegate())
            .build()
    }

    private fun setupAdapterData(){
        compositeAdapter.setItems(listOf(
            QuickPickItem("Quick pick", null),
            QuickPickItem("Quick pick", null),
            QuickPickItem("Quick pick", null)
        ))
        homeViewModel.quickPick.observe(viewLifecycleOwner) { tracks ->
            if (tracks.isNotEmpty()){
                compositeAdapter.updateItem(0,
                    QuickPickItem(title = "Quick pick", track = tracks[0]))
            }
        }
        homeViewModel.userPlaylists.observe(viewLifecycleOwner) {playlists ->
            if (playlists.isNotEmpty()){
                compositeAdapter.updateItem(1,
                    HorizontalItems(
                        title = "Liked playlists",
                        adapter = CardAdapter(),
                        items = playlists.playlistCardItems { onPlayListClick(it) }))
            }
        }
        homeViewModel.trackList.observe(viewLifecycleOwner) {tracks ->
            if (tracks.isNotEmpty()){
                compositeAdapter.updateItem(2,
                    HorizontalItems(
                        title ="Linkin Park",
                        adapter = LargeTracksAdapter(),
                        items = tracks.toLargeThumbnailItems()))
            }
        }
    }

    private fun onPlayListClick(playList: PlayList){
        playListViewModel.setPlaylist(playList)
        navController.navigate(R.id.action_homeFragment_to_albumFragment)
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
                    margin,
                    playerHeight + navbarHeight + margin,
                    margin)
            )
        }
    }

    private fun onPlayPauseClick(track: Track){
        if (playerHandler.currentMediaItemLiveData.value?.mediaId == track.videoId){
            playerHandler.togglePlayPause()
        }
        playerHandler.playNow(track)
    }
}