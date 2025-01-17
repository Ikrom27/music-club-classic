package ru.ikrom.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import dagger.hilt.android.AndroidEntryPoint
import ru.ikrom.searchbar.SearchBar
import ru.ikrom.theme.AppDimens
import ru.ikrom.base_adapter.CompositeAdapter
import ru.ikrom.base_adapter.ThumbnailItem
import ru.ikrom.adapter_delegates.delegates.ThumbnailSmallDelegate
import ru.ikrom.adapter_delegates.delegates.ThumbnailSmallItem
import ru.ikrom.adapter_delegates.delegates.TitleDelegate
import ru.ikrom.base_fragment.DefaultListFragment
import ru.ikrom.placeholder.PlaceholderView
import ru.ikrom.theme.appBottomMargin
import ru.ikrom.theme.appTopMargin
import ru.ikrom.ui.base_adapter.item_decorations.MarginItemDecoration
import javax.inject.Inject

@AndroidEntryPoint
class SearchFragment : DefaultListFragment<SearchUiState, SearchViewModel>(R.layout.fragment_search) {
    @Inject
    lateinit var navigator: Navigator
    override val mViewModel: SearchViewModel by viewModels()
    private lateinit var errorPH: PlaceholderView
    private lateinit var emptyPH: PlaceholderView
    private var mAdapter = CompositeAdapter.Builder()
        .add(ThumbnailSmallDelegate(
            onClick = { mViewModel.playTrack(it) },
            onLongClick = { navigator.toTrackMenu(it) }
        ))
        .add(TitleDelegate())
        .build()

    override fun getAdapter() = mAdapter
    override fun getRecyclerViewId() = R.id.rv_content
    override fun getLayoutManager() = LinearLayoutManager(context)
    override fun setupItemDecorationsList(rv: RecyclerView) = listOf(
        MarginItemDecoration(
            startSpace = resources.appTopMargin(),
            endSpace = resources.appBottomMargin(),
        )
    )

    override fun handleState(state: SearchUiState) {
        hidePlaceHolders()
        mAdapter.setItems(emptyList())
        when (state){
            SearchUiState.Error -> showError()
            SearchUiState.Loading -> {}
            SearchUiState.NoResult -> showEmptyResult()
            is SearchUiState.Success -> showSearchResult(state.data)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindViews(view)
    }

    private fun bindViews(view: View){
        view.findViewById<SearchBar?>(R.id.search_bar).apply {
            doOnTextChanged{ text, _, _, _ ->
                mViewModel.updateSearchRequest(text.toString())
            }
            setOnBackClick { navigator.toUp() }
            setOnCleanClick { mViewModel.updateSearchRequest("") }
        }
        errorPH = view.findViewById(R.id.ph_connection_error)
        emptyPH = view.findViewById(R.id.ph_no_result)
    }

    private fun hidePlaceHolders(){
        emptyPH.visibility = View.GONE
        errorPH.visibility = View.GONE
    }

    private fun showSearchResult(data: List<ThumbnailSmallItem>){
        mAdapter.addAll(data)
    }

    private fun showError(){
        errorPH.title = "Error"
        errorPH.imageSrc = R.drawable.ph_connection_error
        errorPH.visibility = View.VISIBLE
    }

    private fun showEmptyResult(){
        errorPH.visibility = View.GONE
        emptyPH.title = "No result"
        emptyPH.imageSrc = R.drawable.ph_no_result
        emptyPH.visibility = View.VISIBLE
    }

    interface Navigator {
        fun toTrackMenu(item: ThumbnailItem)
        fun toUp()
    }

    companion object {
        private const val TAG = "SearchFragment"
    }
}