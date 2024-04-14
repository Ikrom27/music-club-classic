package com.ikrom.music_club_classic.ui.screens

import android.os.Bundle
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
import com.ikrom.music_club_classic.extensions.toMediumTrackItem
import com.ikrom.music_club_classic.playback.PlayerHandler
import com.ikrom.base_adapter.CompositeAdapter
import com.ikrom.base_adapter.item_decorations.MarginItemDecoration
import com.ikrom.music_club_classic.ui.adapters.delegates.MediumTrackDelegate
import com.ikrom.music_club_classic.ui.adapters.delegates.TitleDelegate
import com.ikrom.music_club_classic.ui.adapters.delegates.TitleItem
import com.ikrom.music_club_classic.ui.components.SearchBar
import com.ikrom.music_club_classic.viewmodel.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SearchFragment : Fragment() {
    @Inject
    lateinit var playerHandler: PlayerHandler

    private val viewModel: SearchViewModel by activityViewModels()

    private lateinit var navController: NavController
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchBar: SearchBar
    private var adapter = CompositeAdapter.Builder()
        .add(MediumTrackDelegate())
        .add(TitleDelegate())
        .build()

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
                adapter.addItems(listOf(TitleItem("This playlist")))
                adapter.addItems(localTracks.map {
                    it.toMediumTrackItem(
                        onItemClick = {playerHandler.playNow(it)},
                        onButtonClick = {}
                    ) })
                adapter.addItems(listOf(TitleItem(" ")))
            }
            if (globalTracks.isNotEmpty()) {
                adapter.addItems(listOf(TitleItem("Global search")))
                adapter.addItems(globalTracks.map {
                    it.toMediumTrackItem(
                        onItemClick = {playerHandler.playNow(it)},
                        onButtonClick = {}) })
            }
        }

    }

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
                endSpace = playerHeight + navbarHeight + margin,
            )
        )
    }

    companion object {
        private const val TAG = "SearchFragment"
    }
}