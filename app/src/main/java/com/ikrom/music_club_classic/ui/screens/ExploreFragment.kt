package com.ikrom.music_club_classic.ui.screens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ikrom.music_club_classic.R
import com.ikrom.music_club_classic.data.model.Album
import com.ikrom.music_club_classic.extensions.albumCardItems
import com.ikrom.music_club_classic.ui.adapters.base_adapters.CompositeAdapter
import com.ikrom.music_club_classic.ui.adapters.delegates.CardAdapter
import com.ikrom.music_club_classic.ui.adapters.delegates.RecyclerViewDelegate
import com.ikrom.music_club_classic.ui.adapters.delegates.RecyclerViewItem
import com.ikrom.music_club_classic.ui.components.AppBar
import com.ikrom.music_club_classic.viewmodel.AlbumViewModel
import com.ikrom.music_club_classic.viewmodel.ExploreViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ExploreFragment : Fragment() {
    private val viewModel: ExploreViewModel by viewModels()
    private val albumViewModel: AlbumViewModel by activityViewModels()

    private lateinit var appBar: AppBar
    private lateinit var navController: NavController

    private lateinit var adapter: CompositeAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_explore, container, false)
        navController = requireParentFragment().findNavController()
        bindViews(view)
        setupButtons(view)
        setupAdapter()
        setupRecyclerView()
        setupAdapterData()
        return view
    }

    private fun setupAdapterData(){
        viewModel.newReleasesList.observe(viewLifecycleOwner){albums ->
            if(albums.isNotEmpty()){
                val cardItems = albums.albumCardItems { onAlbumClick(it) }
                val newReleasesItem = RecyclerViewItem("New Releases", cardItems)
                adapter.updateItem(0, newReleasesItem)
            }
        }
    }

    private fun onAlbumClick(album: Album){
        albumViewModel.setAlbum(album)
        navController.navigate(R.id.exploreFragment_to_albumFragment)
    }

    private fun setupRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
    }

    private fun setupAdapter() {
        adapter = CompositeAdapter.Builder()
            .add(RecyclerViewDelegate(CardAdapter()))
            .build()
    }

    private fun setupButtons(view: View) {
        appBar.setOnSearchClick {
            navController.navigate(R.id.action_exploreFragment_to_searchFragment)
        }
    }

    fun bindViews(view: View){
        appBar = view.findViewById(R.id.action_bar)
        recyclerView = view.findViewById(R.id.rv_content)
    }
}