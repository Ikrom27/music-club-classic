package com.ikrom.music_club_classic.ui.base_adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

abstract class BaseAdapter<T>:
    BaseAdapterHandler<T, BaseAdapter.BaseViewHolder<T>>(){

    abstract fun getViewHolder(binding: View): BaseViewHolder<T>
    abstract fun getLayoutId(): Int

    abstract class BaseViewHolder<T>(itemView: View): RecyclerView.ViewHolder(itemView) {
        abstract fun bind(item: T)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<T> {
        val binding = LayoutInflater.from(parent.context).inflate(getLayoutId(), parent, false)
        return getViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BaseViewHolder<T>, position: Int) {
        holder.bind(mItems[position])
        holder.itemView.setOnClickListener {
            mCallBack?.onItemClick(mItems[position], holder.itemView)
        }
        holder.itemView.setOnLongClickListener {
            if (mCallBack == null){
                false
            } else {
                mCallBack!!.onLongClick(mItems[position], holder.itemView)
                true
            }
        }
    }
}