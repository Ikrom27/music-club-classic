package com.ikrom.music_club_classic.ui.screens

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ikrom.music_club_classic.R
import com.ikrom.music_club_classic.data.model.Track
import com.ikrom.music_club_classic.playback.PlayerHandler
import com.ikrom.music_club_classic.ui.adapters.base_adapters.BaseAdapterCallBack
import com.ikrom.music_club_classic.ui.adapters.base_adapters.item_decorations.MarginItemDecoration
import com.ikrom.music_club_classic.ui.adapters.explore.SearchAdapter
import com.ikrom.music_club_classic.viewmodel.ExploreViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ExploreFragment : Fragment() {
    @Inject
    lateinit var playerHandler: PlayerHandler

    private lateinit var recyclerView: RecyclerView
    private val adapter = SearchAdapter(
        onItemClick =  { onItemClick(it) },
        onMoreButtonClick =  { onMoreButtonClick(it) })
    val viewModel: ExploreViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_explore, container, false)
        bindViews(view)
        setupAdapter()
        setupRecycleView()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.updateSearchList("suzume")
    }

    fun bindViews(view: View){
        recyclerView = view.findViewById(R.id.rv_content)
    }

    fun setupAdapter(){
        viewModel.searchList.observe(viewLifecycleOwner){
            adapter.setItems(it)
        }
        adapter.attachCallBack(object : BaseAdapterCallBack<Track>(){
            override fun onItemClick(item: Track, view: View) {
                onItemClick(item)
            }
        })
    }

    fun onMoreButtonClick(track: Track){}

    fun onItemClick(track: Track){
        playerHandler.playNow(track)
        Log.d("click", "boob")
    }

    fun setupRecycleView(){
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter
        val playerHeight = resources.getDimensionPixelSize(R.dimen.mini_player_height)
        val navbarHeight = resources.getDimensionPixelSize(R.dimen.bottom_nav_bar_height)
        val margin = resources.getDimensionPixelSize(R.dimen.medium_items_margin)
        recyclerView.addItemDecoration(
            MarginItemDecoration(
                margin,
                playerHeight + navbarHeight + margin,
                margin)
        )
    }
}