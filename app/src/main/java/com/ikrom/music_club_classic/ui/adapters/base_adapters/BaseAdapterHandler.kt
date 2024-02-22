package com.ikrom.music_club_classic.ui.adapters.base_adapters

import android.annotation.SuppressLint
import androidx.recyclerview.widget.RecyclerView

abstract class BaseAdapterHandler<T, VH: RecyclerView.ViewHolder>: RecyclerView.Adapter<VH>() {
    protected val mItems = ArrayList<T>()
    protected var mCallBack: BaseAdapterCallBack<T>? = null

    fun attachCallBack(callBack: BaseAdapterCallBack<T>?){
        mCallBack = callBack
    }

    fun detachCallBack(){
        mCallBack = null
    }

    override fun getItemCount(): Int {
        return mItems.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setItems(newItems: List<T>){
        mItems.clear()
        mItems.addAll(newItems)
        notifyDataSetChanged()
    }

    fun addToPosition(position: Int, item: T){
        mItems.add(position, item)
        notifyItemChanged(position)
    }

    fun updateItem(position: Int, item: T){
        mItems.removeAt(position)
        mItems.add(position, item)
        notifyItemChanged(position)
    }

    fun addToStart(item: T){
        addToPosition(0, item)
    }

    fun addToEnd(item: T){
        addToPosition(mItems.size - 1, item)
    }
}