package com.ikrom.base_adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ikrom.base_adapter.model.AdapterItem

abstract class BaseAdapter<T: AdapterItem>:
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
        super.onBindViewHolder(holder, position)
    }
}