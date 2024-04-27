package com.ikrom.music_club_classic.ui.screens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ikrom.music_club_classic.R
import com.ikrom.music_club_classic.extensions.models.toLibraryItems
import com.ikrom.base_adapter.item_decorations.MarginItemDecoration
import com.ikrom.music_club_classic.ui.adapters.LibraryAdapter
import com.ikrom.music_club_classic.viewmodel.LibraryViewModel
import com.ikrom.music_club_classic.viewmodel.PlayListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LibraryFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: LibraryAdapter
    private val viewModel: LibraryViewModel by activityViewModels()
    private val playListViewModel: PlayListViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_library, container, false)
        bindViews(view)
        setupAdapter()
        setupRecyclerView()
        return view
    }

    private fun bindViews(view: View){
        recyclerView = view.findViewById(R.id.rv_playlist)
    }

    private fun setupAdapter(){
        adapter = LibraryAdapter()
        viewModel.getLikedPlayLists().observe(requireActivity()) {
            if (!it.isNullOrEmpty()){
                adapter.setItems(it.toLibraryItems(
                    onButtonClick = {},
                    onItemClick = {playlist ->
                        val bundle = Bundle().also {
                            it.putString("id", playlist.id)
                            it.putString("title", playlist.title)
                            it.putString("thumbnail", playlist.thumbnail)
                            it.putString("artist_name", playlist.artists?.name)
                        }
                        requireParentFragment().findNavController().navigate(R.id.action_libraryFragment_to_albumFragment, bundle)
                    }
                ))
            }
        }
    }

    private fun setupRecyclerView(){
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter
        val playerHeight = resources.getDimensionPixelSize(R.dimen.mini_player_height)
        val navbarHeight = resources.getDimensionPixelSize(R.dimen.bottom_nav_bar_height)
        val margin = resources.getDimensionPixelSize(R.dimen.section_margin)
        if (recyclerView.itemDecorationCount == 0){
            recyclerView.addItemDecoration(
                MarginItemDecoration(
                    margin,
                    playerHeight + navbarHeight + margin,
                    margin
                )
            )
        }
    }
}