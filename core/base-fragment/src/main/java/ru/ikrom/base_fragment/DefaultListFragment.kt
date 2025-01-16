package ru.ikrom.base_fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import androidx.recyclerview.widget.RecyclerView.LayoutManager

abstract class DefaultListFragment<S, VM: DefaultStateViewModel<S>>(layoutId: Int): Fragment(layoutId) {
    protected abstract val mViewModel: VM

    protected abstract fun getAdapter(): RecyclerView.Adapter<*>
    protected abstract fun getRecyclerViewId(): Int
    protected abstract fun getLayoutManager(): LayoutManager
    protected abstract fun handleState(state: S)
    protected open fun setupItemDecorationsList(rv: RecyclerView): List<ItemDecoration> = emptyList()

    private fun recyclerViewConfigure(rv: RecyclerView) {
        rv.adapter = getAdapter()
        rv.layoutManager = getLayoutManager()
        if(rv.itemDecorationCount == 0){
            setupItemDecorationsList(rv).forEach{ decor ->
                rv.addItemDecoration(decor)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<RecyclerView>(getRecyclerViewId()).also {
            recyclerViewConfigure(it)
        }
        mViewModel.state.observe(viewLifecycleOwner) { state ->
            handleState(state)
        }
    }
}