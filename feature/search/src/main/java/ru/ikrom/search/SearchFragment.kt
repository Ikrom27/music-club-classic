package ru.ikrom.search

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import ru.ikrom.ui.base_adapter.delegates.ThumbnailSmallDelegate
import ru.ikrom.ui.base_adapter.delegates.TitleDelegate
import dagger.hilt.android.AndroidEntryPoint
import ru.ikrom.searchbar.SearchBar
import ru.ikrom.ui.base_adapter.CompositeAdapter
import ru.ikrom.ui.base_adapter.item_decorations.MarginItemDecoration
import ru.ikrom.ui.R.dimen

@AndroidEntryPoint
class SearchFragment : Fragment() {

    private val viewModel: SearchViewModel by activityViewModels()

    private lateinit var recyclerView: RecyclerView
    private lateinit var searchBar: SearchBar
    private lateinit var swipeRefresh: SwipeRefreshLayout
    private var adapter = CompositeAdapter.Builder()
        .add(ThumbnailSmallDelegate(
            onClickItem = {
                viewModel.playTrackById(it.id)
            },
            onLongClickItem = {
                viewModel.toTrackMenu(it.id)
            }
        ))
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
        Log.d("SEARCH", "onCreateView")
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        searchBar.doOnTextChanged{ text, _, _, _ ->
            viewModel.updateSearchRequest(text.toString())
        }
        setupButtons()
    }

    private fun bindViews(view: View){
        recyclerView = view.findViewById(R.id.rv_content)
        searchBar = view.findViewById(R.id.search_bar)
        swipeRefresh = view.findViewById(R.id.swipe_refresh)
    }

    private fun setupButtons(){
        searchBar.setOnBackClick {
            viewModel.navigateUp()
        }
        searchBar.setOnCleanClick {
            viewModel.updateSearchRequest("")
        }
    }


    private fun setupAdapter() {
        viewModel.searchUiResult.observe(viewLifecycleOwner) { tracks ->
            adapter.setItems(emptyList())
            adapter.addItems(tracks)
        }
    }

    private fun setupRecycleView(){
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter
        val playerHeight = resources.getDimensionPixelSize(dimen.mini_player_height)
        val navbarHeight = resources.getDimensionPixelSize(dimen.bottom_nav_bar_height)
        val appbarHeight = resources.getDimensionPixelSize(dimen.app_bar_height)
        val margin = resources.getDimensionPixelSize(dimen.items_margin)
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