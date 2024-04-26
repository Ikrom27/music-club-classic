package com.ikrom.music_club_classic.ui.screens

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout
import androidx.activity.addCallback
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MediatorLiveData
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.ikrom.base_adapter.CompositeAdapter
import com.ikrom.base_adapter.item_decorations.MarginItemDecoration
import com.ikrom.music_club_classic.R
import com.ikrom.music_club_classic.data.model.Track
import com.ikrom.music_club_classic.extensions.models.toThumbnailSmallItem
import com.ikrom.music_club_classic.playback.PlayerHandler
import com.ikrom.music_club_classic.ui.adapters.delegates.ThumbnailSmallDelegate
import com.ikrom.music_club_classic.ui.adapters.delegates.TitleDelegate
import com.ikrom.music_club_classic.ui.adapters.delegates.TitleItem
import com.ikrom.music_club_classic.ui.components.PlaceHolderView
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
    private lateinit var swipeRefresh: SwipeRefreshLayout
    private lateinit var phNoResult: PlaceHolderView
    private lateinit var phConnectionError: PlaceHolderView
    private lateinit var suggestionsContainer: FrameLayout
    private var adapter = CompositeAdapter.Builder()
        .add(ThumbnailSmallDelegate())
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
        searchBar.searchField.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH){
                searchBar.searchField.clearFocus()
                closeKeyboard()
                true
            } else {
                false
            }
        }
        searchBar.searchField.setOnFocusChangeListener { _, hasFocus ->
            if(hasFocus){
                suggestionsContainer.visibility = View.VISIBLE
            } else {
                suggestionsContainer.visibility = View.GONE
            }
        }
        viewModel.serverStatus.observe(viewLifecycleOwner) {
            showConnectionErrorPlaceHolder(it == 500)
        }
        setupButtons()
    }

    private fun closeKeyboard(){
        val inputMethodManager = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(searchBar.searchField.windowToken, 0)
    }

    private fun bindViews(view: View){
        navController = requireParentFragment().findNavController()
        recyclerView = view.findViewById(R.id.rv_content)
        searchBar = view.findViewById(R.id.search_bar)
        swipeRefresh = view.findViewById(R.id.swipe_refresh)
        phNoResult = view.findViewById(R.id.ph_no_result)
        phConnectionError = view.findViewById(R.id.ph_connection_error)
        suggestionsContainer = view.findViewById(R.id.suggestions_container)
        swipeRefresh.setProgressViewOffset(
            true,
            resources.getDimensionPixelSize(R.dimen.swipe_refresh_start_margin),
            resources.getDimensionPixelSize(R.dimen.swipe_refresh_end_margin))
    }

    private fun setupButtons(){
        searchBar.setOnBackClick {
            navController.navigateUp()
        }
        searchBar.setOnCleanClick {
            searchBar.searchField.setText("")
        }
        swipeRefresh.setOnRefreshListener{
            viewModel.updateSearchList()
            Handler(Looper.getMainLooper()).postDelayed({
                swipeRefresh.isRefreshing = false
            }, 3000)
        }
    }


    private fun setupAdapter() {
        getCombinedSearchListLiveData().observe(viewLifecycleOwner) { (localTracks, globalTracks) ->
            adapter.setItems(emptyList())
            if (localTracks.isNotEmpty() || globalTracks.isNotEmpty() ){
                swipeRefresh.isRefreshing = false
            }
            addAdapterItems("This playlist", localTracks)
            addAdapterItems("Global search", globalTracks)
            showNoResultPlaceHolder(localTracks.isEmpty() && globalTracks.isEmpty())
        }

    }

    private fun getCombinedSearchListLiveData(): MediatorLiveData<Pair<List<Track>, List<Track>>>{
        return MediatorLiveData<Pair<List<Track>, List<Track>>>().apply {
            addSource(viewModel.localResultList) { localTracks ->
                value = Pair(localTracks, viewModel.globalResultList.value ?: listOf())
            }
            addSource(viewModel.globalResultList) { globalTracks ->
                value = Pair(viewModel.localResultList.value ?: listOf(), globalTracks)
            }
        }
    }

    private fun addAdapterItems(title: String, list: List<Track>) {
        if (list.isNotEmpty()) {
            adapter.addItems(listOf(TitleItem(title)))
            adapter.addItems(list.map {
                it.toThumbnailSmallItem(
                    onItemClick = {playerHandler.playNow(it)},
                    onButtonClick = {}
                )
            })
        }
    }

    private fun showNoResultPlaceHolder(should: Boolean){
        if (should) {
            phNoResult.visibility = View.VISIBLE
        } else {
            phNoResult.visibility = View.GONE
        }
    }

    private fun showConnectionErrorPlaceHolder(should: Boolean){
        if(should){
            phConnectionError.visibility = View.VISIBLE
        } else {
            phConnectionError.visibility = View.GONE
        }
    }

    private fun setupRecycleView(){
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter
        val playerHeight = resources.getDimensionPixelSize(R.dimen.mini_player_height)
        val navbarHeight = resources.getDimensionPixelSize(R.dimen.bottom_nav_bar_height)
        val appbarHeight = resources.getDimensionPixelSize(R.dimen.app_bar_height)
        val margin = resources.getDimensionPixelSize(R.dimen.items_margin)
        recyclerView.addItemDecoration(
            MarginItemDecoration(
                startSpace = appbarHeight + margin,
                endSpace = playerHeight + navbarHeight + margin,
            )
        )
    }

    companion object {
        private const val TAG = "SearchFragment"
    }
}