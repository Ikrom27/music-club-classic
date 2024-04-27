package com.ikrom.music_club_classic.ui.screens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ikrom.music_club_classic.R
import com.ikrom.music_club_classic.data.model.Playlist
import com.ikrom.music_club_classic.data.model.Track
import com.ikrom.music_club_classic.extensions.models.toThumbnailSmallItem
import com.ikrom.music_club_classic.extensions.models.toThumbnailHeaderItem
import com.ikrom.music_club_classic.playback.PlayerHandler
import com.ikrom.base_adapter.CompositeAdapter
import com.ikrom.base_adapter.item_decorations.MarginItemDecoration
import com.ikrom.music_club_classic.extensions.models.toPlaylist
import com.ikrom.music_club_classic.ui.adapters.delegates.ThumbnailSmallDelegate
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

    private lateinit var currentPlaylist: Playlist
    private val viewModel: PlayListViewModel by viewModels()
    private val searchViewModel: SearchViewModel by activityViewModels()
    private lateinit var trackList: LiveData<List<Track>>

    private lateinit var recyclerView: RecyclerView
    private lateinit var albumBar: AlbumBar
    private lateinit var navController: NavController

    private val compositeAdapter = CompositeAdapter.Builder()
        .add(ThumbnailHeaderDelegate())
        .add(ThumbnailSmallDelegate())
        .build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navController = requireParentFragment().findNavController()
        if (arguments != null){
            currentPlaylist = requireArguments().toPlaylist()
            viewModel.updatePlaylistItems(currentPlaylist.id)
        } else {
            Toast.makeText(requireContext(), "Playlist error", Toast.LENGTH_SHORT).show()
            navController.navigateUp()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_playlist, container, false)
        trackList = viewModel.playlistItems
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
        recyclerView = view.findViewById(R.id.rv_content)
        albumBar = view.findViewById(R.id.album_bar)
    }

    private fun setupRecyclerView(){
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = compositeAdapter
        val playerHeight = resources.getDimensionPixelSize(R.dimen.mini_player_height)
        val navbarHeight = resources.getDimensionPixelSize(R.dimen.bottom_nav_bar_height)
        if (recyclerView.itemDecorationCount == 0){
            recyclerView.addItemDecoration(
                MarginItemDecoration(
                    endSpace = playerHeight + navbarHeight
                )
            )
        }
    }

    private fun setupContent() {
        trackList.observe(viewLifecycleOwner) {trackList ->
            val header = currentPlaylist.toThumbnailHeaderItem(
                onPlayClick = {playAll()},
                onShuffleClick = {playShuffled()}
            )
            val tracks = trackList.map{
                it.toThumbnailSmallItem(
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