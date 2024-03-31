package com.ikrom.music_club_classic.ui.screens

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MediatorLiveData
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ikrom.music_club_classic.R
import com.ikrom.music_club_classic.data.model.Track
import com.ikrom.music_club_classic.playback.PlayerHandler
import com.ikrom.music_club_classic.ui.adapters.base_adapters.BaseAdapterCallBack
import com.ikrom.music_club_classic.ui.adapters.base_adapters.CompositeAdapter
import com.ikrom.music_club_classic.ui.adapters.base_adapters.item_decorations.MarginItemDecoration
import com.ikrom.music_club_classic.ui.adapters.search.MediumTrackDelegate
import com.ikrom.music_club_classic.ui.adapters.search.SearchAdapter
import com.ikrom.music_club_classic.ui.adapters.search.TitleDelegate
import com.ikrom.music_club_classic.ui.adapters.search.TitleDelegateItem
import com.ikrom.music_club_classic.ui.adapters.search.TrackDelegateItem
import com.ikrom.music_club_classic.ui.components.SearchBar
import com.ikrom.music_club_classic.viewmodel.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SearchFragment : Fragment() {
    @Inject
    lateinit var playerHandler: PlayerHandler

    private val viewModel: SearchViewModel by activityViewModels()

    private lateinit var adapter: CompositeAdapter
    private lateinit var navController: NavController
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchBar: SearchBar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_search, container, false)
        bindViews(view)
        setupAdapter()
        setupRecycleView()
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner){
            requireParentFragment().findNavController().navigateUp()
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        searchBar.doOnTextChanged{ text, _, _, _ ->
            viewModel.updateSearchList(text.toString())
        }
        setupButtons()
    }

    private fun bindViews(view: View){
        navController = requireParentFragment().findNavController()
        recyclerView = view.findViewById(R.id.rv_content)
        searchBar = view.findViewById(R.id.search_bar)
    }

    private fun setupButtons(){
        searchBar.setOnBackClick {
            navController.navigateUp()
        }
        searchBar.setOnCleanClick {
            searchBar.searchField.setText("")
        }
    }


    fun setupAdapter() {
        adapter = CompositeAdapter.Builder()
            .add(MediumTrackDelegate { onMoreButtonClick(it) })
            .add(TitleDelegate())
            .build()

        val mediatorLiveData = MediatorLiveData<Pair<List<Track>, List<Track>>>().apply {
            addSource(viewModel.localResultList) { localTracks ->
                value = Pair(localTracks, viewModel.globalResultList.value ?: listOf())
            }
            addSource(viewModel.globalResultList) { globalTracks ->
                value = Pair(viewModel.localResultList.value ?: listOf(), globalTracks)
            }
        }

        mediatorLiveData.observe(viewLifecycleOwner) { (localTracks, globalTracks) ->
            adapter.setItems(emptyList())
            if (localTracks.isNotEmpty()) {
                adapter.addItems(listOf(TitleDelegateItem("This playlist")))
                adapter.addItems(localTracks.map { TrackDelegateItem(it) })
                adapter.addItems(listOf(TitleDelegateItem(" ")))
            }
            if (globalTracks.isNotEmpty()) {
                adapter.addItems(listOf(TitleDelegateItem("Global search")))
                adapter.addItems(globalTracks.map { TrackDelegateItem(it) })
            }
        }

    }


    fun onMoreButtonClick(track: Track){}

    fun onItemClick(track: Track){
        playerHandler.playNow(track)
    }

    fun setupRecycleView(){
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter
        val playerHeight = resources.getDimensionPixelSize(R.dimen.mini_player_height)
        val navbarHeight = resources.getDimensionPixelSize(R.dimen.bottom_nav_bar_height)
        val margin = resources.getDimensionPixelSize(R.dimen.medium_items_margin)
        recyclerView.addItemDecoration(
            MarginItemDecoration(
                startSpace = margin,
                endSpace = playerHeight + navbarHeight + margin,)
        )
    }

    companion object {
        private const val TAG = "SearchFragment"
    }
}