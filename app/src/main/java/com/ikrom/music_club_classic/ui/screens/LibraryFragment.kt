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
import ru.ikrom.ui.base_adapter.item_decorations.MarginItemDecoration
import com.ikrom.music_club_classic.ui.adapters.LibraryAdapter
import com.ikrom.music_club_classic.ui.adapters.LibraryItem
import com.ikrom.music_club_classic.viewmodel.LibraryViewModel
import dagger.hilt.android.AndroidEntryPoint
import ru.ikrom.ui.base_adapter.CompositeAdapter

@AndroidEntryPoint
class LibraryFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private val adapter = CompositeAdapter.Builder()
        .add(LibraryAdapter(
            onClick = { onItemClick(it) },
            onLongClick = {}
        ))
        .build()
    private val viewModel: LibraryViewModel by activityViewModels()

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
        viewModel.likedPlaylists.observe(requireActivity()) {
            adapter.setItems(it)
        }
    }

    private fun onItemClick(item: LibraryItem){
        val bundle = Bundle().also {
            it.putString("id", item.id)
            it.putString("title", item.title)
            it.putString("thumbnail", item.thumbnail)
            it.putString("artist_name", item.subtitle)
        }
        requireParentFragment().findNavController().navigate(R.id.library_to_playlist, bundle)
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