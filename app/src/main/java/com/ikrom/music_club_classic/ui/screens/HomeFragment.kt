package com.ikrom.music_club_classic.ui.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ikrom.music_club_classic.R
import com.ikrom.music_club_classic.data.model.Track
import com.ikrom.music_club_classic.playback.PlayerHandler
import com.ikrom.music_club_classic.ui.adapters.base_adapters.BaseAdapterCallBack
import com.ikrom.music_club_classic.ui.adapters.base_adapters.CompositeAdapter
import com.ikrom.music_club_classic.ui.adapters.base_adapters.item_decorations.MarginItemDecoration
import com.ikrom.music_club_classic.ui.adapters.home.ArtistTracksDelegate
import com.ikrom.music_club_classic.ui.adapters.home.AuthorTracksDelegateItem
import com.ikrom.music_club_classic.ui.adapters.home.CardDelegate
import com.ikrom.music_club_classic.ui.adapters.home.CardDelegateItem
import com.ikrom.music_club_classic.ui.adapters.home.PlayerCardDelegate
import com.ikrom.music_club_classic.ui.adapters.home.PlayerDelegateItem
import com.ikrom.music_club_classic.viewmodel.HomeViewModel
import com.ikrom.music_club_classic.viewmodel.PlayListViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {
    @Inject
    lateinit var playerHandler: PlayerHandler
    private val homeViewModel: HomeViewModel by activityViewModels()
    private val playListViewModel: PlayListViewModel by activityViewModels()
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.rv_main)
        navController = requireParentFragment().findNavController()
        setupAdapter(recyclerView)
        return view
    }

    private fun setupAdapter(recyclerView: RecyclerView){
        val compositeAdapter = CompositeAdapter.Builder()
            .add(ArtistTracksDelegate(object : BaseAdapterCallBack<Track>(){
                override fun onItemClick(item: Track, view: View) {
                    playerHandler.playNow(item)
                }
            }))
            .add(PlayerCardDelegate(
                isPlaying = playerHandler.isPlayingLiveData,
                currentMediaItem= playerHandler.currentMediaItemLiveData,
                lifecycleOwner = viewLifecycleOwner,
                onPlayPauseClick = {
                    onPlayPauseClick(it)
                },
                onSkipClick = {

                }
            ))
            .add(CardDelegate {
                playListViewModel.setPlaylist(it)
                navController.navigate(R.id.action_homeFragment_to_albumFragment)
            })
            .build()
        val testData = listOf(
            PlayerDelegateItem(title = "Quick pick", track = null),
            CardDelegateItem(title = "Liked playlists", playLists = homeViewModel.getLikedPlayLists()),
            AuthorTracksDelegateItem(title = "Linkin Park", tracks = homeViewModel.getTracks("Linkin Park")),
        )
        compositeAdapter.setItems(testData)
        homeViewModel.quickPick.observe(viewLifecycleOwner) { tracks ->
            if(tracks.isNotEmpty()){
                compositeAdapter.updateItem(0, PlayerDelegateItem(title = "Quick pick", track = tracks[0]))
            }
        }
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