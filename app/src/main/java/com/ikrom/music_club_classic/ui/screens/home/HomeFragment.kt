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
import com.ikrom.music_club_classic.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private val homeViewModel: HomeViewModel by viewModels()
    private val compositeAdapter = CompositeAdapter.Builder()
        .add(HorizontalTracksDelegate())
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
            HorizontalTracksDelegateItem(title = "Beatles", tracks = homeViewModel.getTracks("Beatles")),
            NewReleasesDelegateItem(title = "New releases", albums = homeViewModel.getNewReleases())
        )
        compositeAdapter.setItems(testData)
        homeViewModel.getTracks("Beatles").observe(viewLifecycleOwner) { tracks ->
            if(tracks.isNotEmpty()){
                compositeAdapter.addToStart(PlayerDelegateItem(title = "Last play", content = tracks.first()))
            }
        }
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = compositeAdapter
    }
}