package com.ikrom.base_adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ikrom.base_adapter.model.AdapterItem


abstract class BaseDelegateAdapter<T : AdapterItem, in VH: BaseDelegateAdapter.DelegateViewHolder<T>>(
    private val classType: Class<out T>
) {
    abstract fun createViewHolder(binding: View): RecyclerView.ViewHolder

    abstract fun getLayoutId(): Int
    abstract class DelegateViewHolder<T: AdapterItem>(itemView: View): RecyclerView.ViewHolder(itemView){
        abstract fun bind(item: T)
    }

    fun createViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val binding = LayoutInflater.from(parent.context).inflate(getLayoutId(), parent, false)
        return createViewHolder(binding)
    }

    fun onBindViewHolder(item: T, viewHolder: RecyclerView.ViewHolder) {
        (viewHolder as DelegateViewHolder<T>).bind(item)
    }

    fun isForViewType(item: AdapterItem): Boolean {
        return item.javaClass == classType
    }
}