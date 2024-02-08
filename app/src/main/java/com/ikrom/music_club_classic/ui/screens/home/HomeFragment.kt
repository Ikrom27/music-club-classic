package com.ikrom.music_club_classic.ui.screens.home

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ikrom.music_club_classic.R
import com.ikrom.music_club_classic.ui.base_adapters.CompositeAdapter
import com.ikrom.music_club_classic.ui.base_adapters.item_decorations.MarginItemDecoration
import com.ikrom.music_club_classic.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.Dispatcher

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private val homeViewModel: HomeViewModel by viewModels()
    private val compositeAdapter = CompositeAdapter.Builder()
        .add(AuthorTracksDelegate())
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
        //Add sections
        val testData = listOf(
            AuthorTracksDelegateItem(title = "Beatles", tracks = homeViewModel.getTracks("Beatles")),
            NewReleasesDelegateItem(title = "New releases", albums = homeViewModel.getNewReleases()),
            AuthorTracksDelegateItem(title = "Linkin Park", tracks = homeViewModel.getTracks("Linkin Park")),
            AuthorTracksDelegateItem(title = "21 pilots", tracks = homeViewModel.getTracks("21 pilots")),
        )
        compositeAdapter.setItems(testData)

        //Observe and add new section
        homeViewModel.getTracks("Imagine dragons").observe(viewLifecycleOwner) { tracks ->
            if(tracks.isNotEmpty()){
                compositeAdapter.addToStart(PlayerDelegateItem(title = "Last play", content = tracks.first()))
            }
        }

        //other
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = compositeAdapter
        val padding = resources.getDimensionPixelSize(R.dimen.section_margin)
        recyclerView.addItemDecoration(MarginItemDecoration(padding, padding*10, padding))
    }
}