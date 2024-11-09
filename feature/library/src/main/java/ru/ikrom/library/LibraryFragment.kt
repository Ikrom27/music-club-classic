package ru.ikrom.library

import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import ru.ikrom.ui.CompositeFragment
import ru.ikrom.ui.base_adapter.CompositeAdapter
import ru.ikrom.ui.base_adapter.delegates.ThumbnailSmallDelegate
import ru.ikrom.ui.base_adapter.delegates.ThumbnailSmallItem

@AndroidEntryPoint
class LibraryFragment : CompositeFragment() {
    private val viewModel: LibraryViewModel by viewModels()

    override fun setupAdapter(): CompositeAdapter {
        return CompositeAdapter.Builder()
            .add(ThumbnailSmallDelegate(
                onClickItem = {},
                onLongClickItem = {}
            ))
            .build()
    }

    override fun setupData() {
        viewModel.update()
        viewModel.uiState.observe(viewLifecycleOwner){ state ->
            when(state){
                is UiState.onSuccessful -> showContent(state.data)
                is UiState.onError -> showError()
                is UiState.onLoading -> showLoading()
            }
        }
    }

    fun showContent(data: List<ThumbnailSmallItem>){
        compositeAdapter.setItems(data)
    }

    fun showError(){

    }

    fun showLoading(){

    }
}