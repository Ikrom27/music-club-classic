package com.ikrom.base_adapter

import android.annotation.SuppressLint
import androidx.recyclerview.widget.RecyclerView
import com.ikrom.base_adapter.model.AdapterItem

abstract class BaseAdapterHandler<T: AdapterItem, VH: RecyclerView.ViewHolder>: RecyclerView.Adapter<VH>() {
    protected val mItems = ArrayList<T>()

    override fun getItemCount(): Int {
        return mItems.size
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.itemView.setOnClickListener {
            mItems[position].onClick()
        }
        holder.itemView.setOnLongClickListener {
            mItems[position].onLongClick()
            true
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setItems(newItems: List<T>){
        mItems.clear()
        mItems.addAll(newItems)
        notifyDataSetChanged()
    }

    fun addToPosition(position: Int, item: T){
        if (itemCount <= position){
            mItems.add(item)
            notifyItemChanged(itemCount)
            return
        }
        mItems.add(position, item)
        notifyItemChanged(position)
    }

    fun updateItem(position: Int, item: T){
        if (position >= mItems.size){
            addToEnd(item)
            return
        }
        mItems.removeAt(position)
        mItems.add(position, item)
        notifyItemChanged(position)
    }

    fun addToStart(item: T){
        addToPosition(0, item)
    }

    fun addItems(items: List<T>){
        val startPosition = mItems.size - 1
        mItems += items
        notifyItemRangeChanged(startPosition, mItems.size)
    }

    fun addToEnd(item: T){
        addToPosition(mItems.size, item)
    }
}