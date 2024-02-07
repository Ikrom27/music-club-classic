package com.ikrom.music_club_classic.ui.base_adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

interface IDelegateAdapterItem {
    fun id(): Any

    fun content(): Any

    fun payload(other: Any): Payloadable = Payloadable.None

    interface Payloadable {
        object None: Payloadable
    }
}

abstract class DelegateAdapter<T : IDelegateAdapterItem, in VH: DelegateAdapter.BaseViewHolder>(
    private val classType: Class<out T>
) {
    abstract fun createViewHolder(binding: View): RecyclerView.ViewHolder

    abstract fun getLayoutId(): Int
    abstract class BaseViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        abstract fun bind(item: IDelegateAdapterItem)
    }

    fun createViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val binding = LayoutInflater.from(parent.context).inflate(getLayoutId(), parent, false)
        return createViewHolder(binding)
    }

    fun onBindViewHolder(item: T, viewHolder: RecyclerView.ViewHolder, payloads: List<IDelegateAdapterItem.Payloadable>) {
        (viewHolder as BaseViewHolder).bind(item)
    }

    fun isForViewType(item: IDelegateAdapterItem): Boolean {
        return item.javaClass == classType
    }
}