package com.ikrom.music_club_classic.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

abstract class DelegateAdapter<T : DelegateAdapterItem, in VH: DelegateAdapter.BaseViewHolder>(
    private val classType: Class<out T>
) {
    abstract fun createViewHolder(binding: View): RecyclerView.ViewHolder

    abstract fun getLayoutId(): Int
    abstract class BaseViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        abstract fun bind(item: DelegateAdapterItem)
    }

    fun createViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val binding = LayoutInflater.from(parent.context).inflate(getLayoutId(), parent, false)
        return createViewHolder(binding)
    }

    fun onBindViewHolder(item: T, viewHolder: RecyclerView.ViewHolder, payloads: List<DelegateAdapterItem.Payloadable>) {
        (viewHolder as BaseViewHolder ).bind(item)
    }

    fun isForViewType(item: DelegateAdapterItem): Boolean {
        return item.javaClass == classType
    }


}