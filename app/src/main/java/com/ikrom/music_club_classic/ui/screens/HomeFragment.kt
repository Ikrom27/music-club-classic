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
import com.ikrom.music_club_classic.extensions.models.playlistCardItems
import com.ikrom.music_club_classic.extensions.models.toThumbnailMediumItems
import com.ikrom.music_club_classic.playback.PlayerHandler
import com.ikrom.music_club_classic.ui.adapters.delegates.CardAdapter
import com.ikrom.music_club_classic.ui.adapters.delegates.NestedItemsDelegate
import com.ikrom.music_club_classic.ui.adapters.delegates.NestedItems
import com.ikrom.music_club_classic.ui.adapters.delegates.ThumbnailMediumAdapter
import com.ikrom.music_club_classic.ui.adapters.delegates.TitleDelegate
import com.ikrom.music_club_classic.ui.adapters.delegates.TitleItem
import com.ikrom.music_club_classic.ui.adapters.home.QuickPickDelegate
import com.ikrom.music_club_classic.ui.adapters.home.QuickPickItem
import com.ikrom.music_club_classic.ui.menu.TracksMenu
import com.ikrom.music_club_classic.viewmodel.BottomMenuViewModel
import com.ikrom.music_club_classic.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import ru.ikrom.ui.base_adapter.CompositeAdapter
import ru.ikrom.ui.base_adapter.item_decorations.MarginItemDecoration
import ru.ikrom.youtube_data.model.PlaylistModel
import ru.ikrom.youtube_data.model.TrackModel
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {
    @Inject
    lateinit var playerHandler: PlayerHandler
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
                isPlaying = playerHandler.isPlayingLiveData,
                currentMediaItem= playerHandler.currentMediaItemLiveData,
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
                        items = playlists.playlistCardItems { onPlayListClick(it) },
                        adapter = CardAdapter()
                    )
                )
            }
        }
        homeViewModel.trackList.observe(viewLifecycleOwner) {tracks ->
            if (tracks.isNotEmpty()){
                compositeAdapter.updateItem(4, TitleItem("From Linkin park"))
                compositeAdapter.updateItem(5,
                    NestedItems(
                        adapter = ThumbnailMediumAdapter(),
                        items = tracks.toThumbnailMediumItems(
                            onClick = {
                                playerHandler.playNow(it)
                            },
                            onLongClick = {
                                bottomMenuViewModel.trackLiveData.postValue(it)
                                val bottomMenu = TracksMenu()
                                bottomMenu.show(parentFragmentManager, bottomMenu.tag)
                            }
                        )
                    )
                )
            }
        }
    }

    private fun onPlayListClick(playList: PlaylistModel){
        val bundle = Bundle().also {
            it.putString("id", playList.id)
            it.putString("title", playList.title)
            it.putString("thumbnail", playList.thumbnail)
            it.putString("artist_name", playList.artists?.name)
        }
        navController.navigate(R.id.explore_to_playlist, bundle)
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
        if (playerHandler.currentMediaItemLiveData.value?.mediaId == track.videoId){
            playerHandler.togglePlayPause()
        }
        playerHandler.playNow(track)
    }
}