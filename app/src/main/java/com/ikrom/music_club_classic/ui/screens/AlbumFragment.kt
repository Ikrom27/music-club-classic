package com.ikrom.music_club_classic.ui.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ikrom.music_club_classic.R
import com.ikrom.music_club_classic.ui.adapters.delegates.ThumbnailHeaderDelegate
import com.ikrom.music_club_classic.ui.adapters.delegates.ThumbnailSmallDelegate
import com.ikrom.music_club_classic.ui.components.AlbumBar
import com.ikrom.music_club_classic.viewmodel.AlbumViewModel
import dagger.hilt.android.AndroidEntryPoint
import ru.ikrom.ui.base_adapter.CompositeAdapter
import ru.ikrom.ui.base_adapter.item_decorations.MarginItemDecoration

@AndroidEntryPoint
class AlbumFragment : Fragment() {
    private val viewModel: AlbumViewModel by activityViewModels()

    private lateinit var recyclerView: RecyclerView
    private lateinit var albumBar: AlbumBar
    private lateinit var navController: NavController

    private val compositeAdapter = CompositeAdapter.Builder()
        .add(ThumbnailHeaderDelegate(
            onPlayClick = {
                viewModel.playAllTracks()
            },
            onShuffleClick = {
                viewModel.playShuffled()
            }
        ))
        .add(ThumbnailSmallDelegate(
            onClickItem = {
                viewModel.playTrackById(it.id)
            },
            onLongClickItem = {})
        )
        .build()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_album, container, false)
        navController = requireParentFragment().findNavController()
        bindViews(view)
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
        albumBar.setOnSearchClick { }
        albumBar.setOnMoreClick { }
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
        viewModel.albumTracks.observe(viewLifecycleOwner) { albumTracks ->
            compositeAdapter.setItems(
                listOf(viewModel.albumInfo, albumTracks)
            )
        }
    }
}