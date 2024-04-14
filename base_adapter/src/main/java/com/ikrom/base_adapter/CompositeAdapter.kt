package com.ikrom.base_adapter

import android.util.SparseArray
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ikrom.base_adapter.model.AdapterItem

class CompositeAdapter(
    private val delegates: SparseArray<BaseDelegateAdapter<AdapterItem, BaseDelegateAdapter.DelegateViewHolder<AdapterItem>>>
): BaseAdapterHandler<AdapterItem, RecyclerView.ViewHolder>() {

    override fun getItemViewType(position: Int): Int {
        val currentItem = mItems[position]
        for (i in 0 until delegates.size()){
            if(delegates[i].isForViewType(currentItem)){
                return delegates.keyAt(i)
            }
        }
        throw NullPointerException("Can not get viewType for position $position")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return delegates[viewType].createViewHolder(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        onBindViewHolder(holder, position, mutableListOf())
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int, payloads: MutableList<Any>) {
        val delegateAdapter = delegates[getItemViewType(position)]

        if (delegateAdapter != null) {
            delegateAdapter.onBindViewHolder(mItems[position], holder)
        } else {
            throw NullPointerException("can not find adapter for position $position")
        }
        super.onBindViewHolder(holder, position)
    }

    class Builder(){
        private var count: Int = 0
        private val delegates: SparseArray<BaseDelegateAdapter<AdapterItem, BaseDelegateAdapter.DelegateViewHolder<AdapterItem>>> = SparseArray()

        fun add(delegateAdapter: BaseDelegateAdapter<out AdapterItem, *>): Builder {
            delegates.put(count++, delegateAdapter as BaseDelegateAdapter<AdapterItem, BaseDelegateAdapter.DelegateViewHolder<AdapterItem>>)
            return this
        }

        fun build(): CompositeAdapter {
            require(count != 0) { "Register at least one adapter" }
            return CompositeAdapter(delegates)
        }
    }
}