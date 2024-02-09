package com.ikrom.music_club_classic.ui.screens.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ikrom.music_club_classic.R
import com.ikrom.music_club_classic.ui.base_adapters.CompositeAdapter
import com.ikrom.music_club_classic.ui.base_adapters.item_decorations.MarginItemDecoration
import com.ikrom.music_club_classic.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private val homeViewModel: HomeViewModel by viewModels()
    private val compositeAdapter = CompositeAdapter.Builder()
        .add(ArtistTracksDelegate())
        .add(PlayerCardDelegate())
        .add(NewReleasesDelegate())
        .build()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.rv_main)
        setAdapter(recyclerView)
        return view
    }

    fun setAdapter(recyclerView: RecyclerView){
        val testData = listOf(
            AuthorTracksDelegateItem(title = "Beatles", tracks = homeViewModel.getTracks("Beatles")),
            NewReleasesDelegateItem(title = "New releases", albums = homeViewModel.getNewReleases()),
            AuthorTracksDelegateItem(title = "Linkin Park", tracks = homeViewModel.getTracks("Linkin Park"))
        )
        compositeAdapter.setItems(testData)
        homeViewModel.getTracks("Imagine dragons").observe(viewLifecycleOwner) { tracks ->
            if(tracks.isNotEmpty()){
                compositeAdapter.addToStart(PlayerDelegateItem(title = "Last play", track = tracks.first()))
            }
        }
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = compositeAdapter
        val padding = resources.getDimensionPixelSize(R.dimen.section_margin)
        if (recyclerView.itemDecorationCount == 0){
            recyclerView.addItemDecoration(MarginItemDecoration(padding, padding*10, padding))
        }
    }
}