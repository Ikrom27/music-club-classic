package com.ikrom.music_club_classic.ui.adapters

import android.util.SparseArray
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class CompositeAdapter(
    private val delegates: SparseArray<DelegateAdapter<DelegateAdapterItem, DelegateAdapter.BaseViewHolder>>
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val items = ArrayList<DelegateAdapterItem>()

    override fun getItemViewType(position: Int): Int {
        val currentItem = items[position]
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

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        onBindViewHolder(holder, position, mutableListOf())
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int, payloads: MutableList<Any>) {
        val delegateAdapter = delegates[getItemViewType(position)]

        if (delegateAdapter != null) {
            val delegatePayloads = payloads.map { it as DelegateAdapterItem.Payloadable }
            delegateAdapter.onBindViewHolder(items[position], holder, delegatePayloads)
        } else {
            throw NullPointerException("can not find adapter for position $position")
        }
    }

    fun swapData(newData: List<DelegateAdapterItem>){
        items.clear()
        items.addAll(newData)
    }

    class Builder(){
        private var count: Int = 0
        private val delegates: SparseArray<DelegateAdapter<DelegateAdapterItem, DelegateAdapter.BaseViewHolder>> = SparseArray()

        fun add(delegateAdapter: DelegateAdapter<out DelegateAdapterItem, *>): Builder {
            delegates.put(count++, delegateAdapter as DelegateAdapter<DelegateAdapterItem, DelegateAdapter.BaseViewHolder>)
            return this
        }

        fun build(): CompositeAdapter {
            require(count != 0) { "Register at least one adapter" }
            return CompositeAdapter(delegates)
        }
    }
}