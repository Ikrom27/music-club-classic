package com.ikrom.music_club_classic.ui.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ikrom.music_club_classic.R
import com.ikrom.music_club_classic.data.model.Track
import com.ikrom.music_club_classic.extensions.playlistCardItems
import com.ikrom.music_club_classic.playback.PlayerHandler
import com.ikrom.music_club_classic.ui.adapters.base_adapters.BaseAdapterCallBack
import com.ikrom.music_club_classic.ui.adapters.base_adapters.CompositeAdapter
import com.ikrom.music_club_classic.ui.adapters.base_adapters.item_decorations.MarginItemDecoration
import com.ikrom.music_club_classic.ui.adapters.delegates.CardAdapter
import com.ikrom.music_club_classic.ui.adapters.delegates.RecyclerViewDelegate
import com.ikrom.music_club_classic.ui.adapters.delegates.RecyclerViewItem
import com.ikrom.music_club_classic.ui.adapters.home.ArtistTracksDelegate
import com.ikrom.music_club_classic.ui.adapters.home.AuthorTracksDelegateItem
import com.ikrom.music_club_classic.ui.adapters.home.QuickPickDelegate
import com.ikrom.music_club_classic.ui.adapters.home.QuickPickItem
import com.ikrom.music_club_classic.ui.components.BottomMenuFragment
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

    private val compositeAdapter = CompositeAdapter.Builder()
        .add(QuickPickDelegate(
            isPlaying = playerHandler.isPlayingLiveData,
            currentMediaItem= playerHandler.currentMediaItemLiveData,
            lifecycleOwner = viewLifecycleOwner,
            onPlayPauseClick = { onPlayPauseClick(it) },
            onSkipClick = {}
        ))
        .add(RecyclerViewDelegate(CardAdapter()))
        .add(ArtistTracksDelegate(object : BaseAdapterCallBack<Track>(){
            override fun onItemClick(item: Track, view: View) {
                playerHandler.playNow(item)
            }

            override fun onLongClick(item: Track, view: View) {
                bottomMenuViewModel.trackLiveData.postValue(item)
                val bottomMenu = BottomMenuFragment()
                bottomMenu.show(parentFragmentManager, bottomMenu.tag)
            }
        }))
        .build()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.rv_main)
        navController = requireParentFragment().findNavController()
        setupRecyclerView(recyclerView)
        setupAdapterData()
        return view
    }

    private fun setupAdapterData(){
        val homeList = MutableLiveData(
            mutableListOf(
                QuickPickItem("", track = null),
                RecyclerViewItem("", emptyList()),
                AuthorTracksDelegateItem("", MutableLiveData(emptyList()))
            )
        )
        homeViewModel.getRecommendedTracks().observe(viewLifecycleOwner) { tracks ->
            homeList.value?.set(0, QuickPickItem(title = "Quick pick", track = tracks[0]))
        }
        homeViewModel.getLikedPlayLists().observe(viewLifecycleOwner) {playlist ->
            homeList.value?.set(1, RecyclerViewItem("Liked playlists", playlist.playlistCardItems {  }))
        }
        homeViewModel.getTracks("Linkin Park").observe(viewLifecycleOwner) {tracks ->
            homeList.value?.set(2, AuthorTracksDelegateItem("Linkin Park", MutableLiveData(tracks)))
        }
        homeList.observe(viewLifecycleOwner){
            compositeAdapter.setItems(it)
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