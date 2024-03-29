package com.ikrom.music_club_classic.ui.screens

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.addCallback
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LiveData
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ikrom.music_club_classic.R
import com.ikrom.music_club_classic.data.model.PlayList
import com.ikrom.music_club_classic.data.model.Track
import com.ikrom.music_club_classic.playback.PlayerHandler
import com.ikrom.music_club_classic.ui.adapters.base_adapters.CompositeAdapter
import com.ikrom.music_club_classic.ui.adapters.base_adapters.IDelegateItem
import com.ikrom.music_club_classic.ui.adapters.base_adapters.item_decorations.MarginItemDecoration
import com.ikrom.music_club_classic.ui.adapters.playlist.PlaylistHeaderDelegate
import com.ikrom.music_club_classic.ui.adapters.playlist.PlaylistHeaderDelegateItem
import com.ikrom.music_club_classic.ui.adapters.playlist.PlaylistTrackAdapter
import com.ikrom.music_club_classic.ui.adapters.playlist.PlaylistTrackDelegateItem
import com.ikrom.music_club_classic.ui.components.AlbumBar
import com.ikrom.music_club_classic.viewmodel.PlayListViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PlaylistFragment : Fragment() {
    @Inject
    lateinit var playerHandler: PlayerHandler

    private var currentPlaylist: PlayList? = null
    private val viewModel: PlayListViewModel by activityViewModels()
    private lateinit var trackList: LiveData<List<Track>>

    private lateinit var recyclerView: RecyclerView
    private lateinit var compositeAdapter: CompositeAdapter
    private lateinit var albumBar: AlbumBar
    private lateinit var navController: NavController


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_playlist, container, false)
        currentPlaylist = viewModel.currentPlaylist.value
        trackList = viewModel.playlistItems
        navController = requireParentFragment().findNavController()
        bindViews(view)
        setupAdapter()
        setupRecyclerView()
        setupContent()
        setupButtons()
        return view
    }

    private fun setupButtons(){
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner){
            navController.navigateUp()
        }
        albumBar.setOnBackClick {
            navController.navigateUp()
        }
        albumBar.setOnSearchClick {
            //TODO: setOnSearchClick
        }
        albumBar.setOnMoreClick {
           //TODO: setOnMoreClick
        }
    }

    private fun setupAdapter() {
        compositeAdapter = CompositeAdapter.Builder()
            .add(PlaylistHeaderDelegate(
                onPlayClick = {
                    trackList.observe(viewLifecycleOwner) {
                        if (it.isNotEmpty()){
                            playerHandler.playNow(it)
                        }
                    }
                },
                onShuffleClick = {
                    trackList.observe(viewLifecycleOwner) {
                        if (it.isNotEmpty()){
                            playerHandler.playNow(it.shuffled())
                        }
                    }
                }
            ))
            .add(PlaylistTrackAdapter(
                onItemClick = {
                    playerHandler.playNow(it)
                },
                onMoreButtonClick = {}
            ))
            .build()
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
                MarginItemDecoration(
                    startSpace = appBarHeight,
                    endSpace = playerHeight + navbarHeight
                )
            )
        }
    }

    private fun setupContent() {
        trackList.observe(viewLifecycleOwner) {trackList ->
            val items = trackList.map { PlaylistTrackDelegateItem(it) }
            compositeAdapter.setItems(
                listOf(PlaylistHeaderDelegateItem(currentPlaylist!!)) + items
            )
        }
    }

}