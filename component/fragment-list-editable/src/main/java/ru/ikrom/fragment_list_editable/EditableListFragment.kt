package ru.ikrom.fragment_list_editable

import android.os.Bundle
import android.view.View
import androidx.core.widget.ContentLoadingProgressBar
import androidx.recyclerview.widget.LinearLayoutManager
import ru.ikrom.base_fragment.DefaultListFragment
import ru.ikrom.theme.AppDrawableIds
import ru.ikrom.ui.placeholder.PlaceHolderView


abstract class EditableListFragment<T, VM: EditableStateViewModel<T>>:
    DefaultListFragment<EditableUiState<T>, VM>(R.layout.fragment_list_editable) {
    abstract override val mViewModel: VM
    private lateinit var mPlaceholder: PlaceHolderView
    private lateinit var mLoading: ContentLoadingProgressBar
    override fun getRecyclerViewId() = R.id.rv_content
    override fun getLayoutManager() = LinearLayoutManager(context)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mPlaceholder = view.findViewById(R.id.placeholder)
        mLoading = view.findViewById(R.id.loading)
    }

    override fun handleState(state: EditableUiState<T>) {
        mLoading.hide()
        mPlaceholder.hide()
        mPlaceholder
        when(state){
            is EditableUiState.Success -> onStateSuccess(state.items)
            is EditableUiState.Loading -> onStateLoading()
            is EditableUiState.Error -> onStateError()
            EditableUiState.Empty -> onStateEmpty()
        }
    }

    abstract fun onStateSuccess(data: List<T>)
    fun onStateLoading() {
        mLoading.show()
    }
    fun onStateError() {
        mPlaceholder.imageSrc = AppDrawableIds.PH_ERROR
        mPlaceholder.show()
    }
    fun onStateEmpty() {
        mPlaceholder.imageSrc = AppDrawableIds.PH_NO_RESULT
        mPlaceholder.show()
    }
}