package com.ikrom.music_club_classic.ui.screens

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.addCallback
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ikrom.music_club_classic.R
import com.ikrom.music_club_classic.data.model.PlayList
import com.ikrom.music_club_classic.ui.adapters.base_adapters.CompositeAdapter
import com.ikrom.music_club_classic.ui.adapters.base_adapters.IDelegateItem
import com.ikrom.music_club_classic.ui.adapters.playlist.PlaylistHeaderDelegate
import com.ikrom.music_club_classic.ui.adapters.playlist.PlaylistHeaderDelegateItem
import com.ikrom.music_club_classic.ui.adapters.playlist.PlaylistTrackAdapter
import com.ikrom.music_club_classic.ui.adapters.playlist.PlaylistTrackDelegateItem
import com.ikrom.music_club_classic.viewmodel.PlayListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PlaylistFragment : Fragment() {
    private var currentPlaylist: PlayList? = null
    private val viewModel: PlayListViewModel by activityViewModels()

    private lateinit var recyclerView: RecyclerView
    private lateinit var compositeAdapter: CompositeAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_playlist, container, false)
        currentPlaylist = viewModel.currentPlaylist.value
        setupAdapter()
        bindViews(view)
        setupContent()
        setBackButton()
        return view
    }

    private fun setupAdapter() {
        compositeAdapter = CompositeAdapter.Builder()
            .add(PlaylistHeaderDelegate())
            .add(PlaylistTrackAdapter(
                onItemClick = {},
                onMoreButtonClick = {}
            ))
            .build()
    }

    private fun setBackButton(){
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner){
            requireParentFragment().findNavController().navigateUp()
        }
    }

    private fun bindViews(view: View){
        recyclerView = view.findViewById(R.id.rv_tracks)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = compositeAdapter
    }

    private fun setupContent() {
        viewModel.getPlaylistItems().observe(viewLifecycleOwner) {trackList ->
            val items = trackList.map { PlaylistTrackDelegateItem(it) }
            compositeAdapter.setItems(
                listOf(PlaylistHeaderDelegateItem(currentPlaylist!!)) + items
            )
        }
    }

}