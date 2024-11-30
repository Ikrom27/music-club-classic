package ru.ikrom.ui.base_adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

open class AdapterItem

abstract class DelegateAdapter<T: Any, in VH: DelegateAdapter.ViewHolder<T>>(
    private val classType: Class<out T>,
    private val onClickItem: ((T) -> Unit)? = null,
    private val onLongClickItem: ((T) -> Unit)? = null
) {
    abstract fun getViewHolder(binding: View): RecyclerView.ViewHolder
    abstract fun getLayoutId(): Int

    abstract class ViewHolder<T>(itemView: View): RecyclerView.ViewHolder(itemView){
        abstract fun bind(item: T)
    }

    fun getViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val binding = LayoutInflater.from(parent.context).inflate(getLayoutId(), parent, false)
        return getViewHolder(binding)
    }

    fun onBindViewHolder(item: T, viewHolder: RecyclerView.ViewHolder) {
        (viewHolder as ViewHolder<T>).bind(item)
        bindClickListeners(viewHolder.itemView, item)
    }

    fun isForViewType(item: T): Boolean {
        return item.javaClass == classType
    }

    private fun bindClickListeners(itemView: View, item: T){
        onClickItem?.let { itemView.setOnClickListener{ it(item) } }
        onLongClickItem?.let { itemView.setOnLongClickListener{
            it(item)
            true
        } }
    }
}