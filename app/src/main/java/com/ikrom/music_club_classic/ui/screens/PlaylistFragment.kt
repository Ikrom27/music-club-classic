package com.ikrom.music_club_classic.ui.screens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LiveData
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ikrom.music_club_classic.R
import com.ikrom.music_club_classic.data.model.PlayList
import com.ikrom.music_club_classic.data.model.Track
import com.ikrom.music_club_classic.extensions.toMediumTrackItem
import com.ikrom.music_club_classic.extensions.toThumbnailHeaderItem
import com.ikrom.music_club_classic.playback.PlayerHandler
import com.ikrom.base_adapter.CompositeAdapter
import com.ikrom.base_adapter.item_decorations.MarginItemDecoration
import com.ikrom.music_club_classic.ui.adapters.delegates.MediumTrackDelegate
import com.ikrom.music_club_classic.ui.adapters.delegates.ThumbnailHeaderDelegate
import com.ikrom.music_club_classic.ui.components.AlbumBar
import com.ikrom.music_club_classic.viewmodel.PlayListViewModel
import com.ikrom.music_club_classic.viewmodel.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PlaylistFragment : Fragment() {
    @Inject
    lateinit var playerHandler: PlayerHandler

    private var currentPlaylist: PlayList? = null
    private val viewModel: PlayListViewModel by activityViewModels()
    private val searchViewModel: SearchViewModel by activityViewModels()
    private lateinit var trackList: LiveData<List<Track>>

    private lateinit var recyclerView: RecyclerView
    private lateinit var albumBar: AlbumBar
    private lateinit var navController: NavController

    private val compositeAdapter = com.ikrom.base_adapter.CompositeAdapter.Builder()
        .add(ThumbnailHeaderDelegate())
        .add(MediumTrackDelegate())
        .build()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_playlist, container, false)
        currentPlaylist = viewModel.currentPlaylist.value
        trackList = viewModel.playlistItems
        navController = requireParentFragment().findNavController()
        bindViews(view)
        setupRecyclerView()
        setupContent()
        setupButtons()
        return view
    }

    private fun playAll(){
        trackList.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()){
                playerHandler.playNow(it)
            }
        }
    }

    private fun playShuffled(){
        trackList.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()){
                playerHandler.playNow(it.shuffled())
            }
        }
    }


    private fun setupButtons(){
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner){
            navController.navigateUp()
        }
        albumBar.setOnBackClick {
            navController.navigateUp()
        }
        albumBar.setOnSearchClick {
            searchViewModel.addContentList(trackList.value!!)
            navController.navigate(R.id.action_playlistFragment_to_searchFragment)
        }
        albumBar.setOnMoreClick {
           //TODO: setOnMoreClick
        }
    }

    private fun bindViews(view: View){
        recyclerView = view.findViewById(R.id.rv_tracks)
        albumBar = view.findViewById(R.id.album_bar)
    }

    private fun setupRecyclerView(){
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = compositeAdapter
        val playerHeight = resources.getDimensionPixelSize(R.dimen.mini_player_height)
        val navbarHeight = resources.getDimensionPixelSize(R.dimen.bottom_nav_bar_height)
        val appBarHeight =  resources.getDimensionPixelSize(R.dimen.app_bar_height)
        if (recyclerView.itemDecorationCount == 0){
            recyclerView.addItemDecoration(
                com.ikrom.base_adapter.item_decorations.MarginItemDecoration(
                    startSpace = appBarHeight,
                    endSpace = playerHeight + navbarHeight
                )
            )
        }
    }

    private fun setupContent() {
        trackList.observe(viewLifecycleOwner) {trackList ->
            val header = currentPlaylist!!.toThumbnailHeaderItem(
                onPlayClick = {playerHandler.playNow(trackList)},
                onShuffleClick = {playerHandler.playNow(trackList.shuffled())}
            )
            val tracks = trackList.map{
                it.toMediumTrackItem(
                    onItemClick = {
                        playerHandler.playNow(it)
                    },
                    onButtonClick = {}
                )
            }
            compositeAdapter.setItems(
                listOf(header) + tracks
            )
        }
    }
}