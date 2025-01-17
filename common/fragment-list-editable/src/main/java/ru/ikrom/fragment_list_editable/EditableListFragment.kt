package ru.ikrom.fragment_list_editable

import android.os.Bundle
import android.view.View
import androidx.core.widget.ContentLoadingProgressBar
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import ru.ikrom.base_fragment.DefaultListFragment
import ru.ikrom.placeholder.PlaceholderView
import ru.ikrom.searchbar.SearchBar
import ru.ikrom.theme.AppDrawableIds
import ru.ikrom.theme.appBottomMargin
import ru.ikrom.theme.appTopMargin
import ru.ikrom.ui.base_adapter.item_decorations.MarginItemDecoration


abstract class EditableListFragment<T, VM: EditableStateViewModel<T>>:
    DefaultListFragment<EditableUiState<T>, VM>(R.layout.fragment_list_editable) {
    abstract override val mViewModel: VM
    private lateinit var mPlaceholder: PlaceholderView
    private lateinit var mLoading: ContentLoadingProgressBar
    private lateinit var swipeRefresh: SwipeRefreshLayout
    override fun getRecyclerViewId() = R.id.rv_content
    override fun getLayoutManager() = LinearLayoutManager(context)

    override fun setupItemDecorationsList(rv: RecyclerView): List<RecyclerView.ItemDecoration> {
        return listOf(MarginItemDecoration(
            startSpace = resources.appTopMargin(),
            endSpace = resources.appBottomMargin()
        ))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mPlaceholder = view.findViewById(R.id.placeholder)
        mLoading = view.findViewById(R.id.loading)
        view.findViewById<SearchBar>(R.id.search_bar).apply {
            doOnTextChanged { text, _, _, _ ->
                mViewModel.update(text.toString())
            }
            setOnBackClick {
                findNavController().navigateUp()
            }
        }
        swipeRefresh = view.findViewById<SwipeRefreshLayout>(R.id.swipe_refresh).apply {
            setOnRefreshListener {
                mViewModel.update()
            }
            setProgressViewOffset(true, 0, resources.appTopMargin() * 2)
        }
    }

    override fun handleState(state: EditableUiState<T>) {
        mLoading.hide()
        mPlaceholder.hide()
        when(state){
            is EditableUiState.Success -> {
                onStateSuccess(state.items)
                swipeRefresh.isRefreshing = false
            }
            is EditableUiState.Loading -> onStateLoading()
            is EditableUiState.Error -> onStateError()
            EditableUiState.Empty -> onStateEmpty()
        }
    }

    abstract fun onStateSuccess(data: List<T>)
    protected fun onStateLoading() {
        mLoading.show()
    }
    protected fun onStateError() {
        mPlaceholder.imageSrc = AppDrawableIds.PH_ERROR
        mPlaceholder.show()
    }
    protected fun onStateEmpty() {
        mPlaceholder.imageSrc = AppDrawableIds.PH_NO_RESULT
        mPlaceholder.show()
    }
}