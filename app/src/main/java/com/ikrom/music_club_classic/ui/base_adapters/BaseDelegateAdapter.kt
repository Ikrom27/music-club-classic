package com.ikrom.music_club_classic.ui.base_adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

interface IDelegateItem {
    fun payload(other: Any): Payloadable = Payloadable.None

    interface Payloadable {
        object None: Payloadable
    }
}

abstract class BaseDelegateAdapter<T : IDelegateItem, in VH: BaseDelegateAdapter.DelegateViewHolder<T>>(
    private val classType: Class<out T>
) {
    abstract fun createViewHolder(binding: View): RecyclerView.ViewHolder

    abstract fun getLayoutId(): Int
    abstract class DelegateViewHolder<T: IDelegateItem>(itemView: View): RecyclerView.ViewHolder(itemView){
        abstract fun bind(item: T)
    }

    fun createViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val binding = LayoutInflater.from(parent.context).inflate(getLayoutId(), parent, false)
        return createViewHolder(binding)
    }

    fun onBindViewHolder(item: T, viewHolder: RecyclerView.ViewHolder) {
        (viewHolder as DelegateViewHolder<T>).bind(item)
    }

    fun isForViewType(item: IDelegateItem): Boolean {
        return item.javaClass == classType
    }
}