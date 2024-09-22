package ru.ikrom.ui.base_adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.ikrom.ui.base_adapter.model.AdapterItem


abstract class DelegateAdapter<T : AdapterItem, in VH: DelegateAdapter.ViewHolder<T>>(
    private val classType: Class<out T>,
//    val onClickItem: ((T) -> Unit)? = null,
//    val onLongClickItem: ((T) -> Unit)? = null
) {
    abstract fun getViewHolder(binding: View): RecyclerView.ViewHolder
    abstract fun getLayoutId(): Int

    abstract class ViewHolder<T: AdapterItem>(itemView: View): RecyclerView.ViewHolder(itemView){
        abstract fun bind(item: T)
    }

    fun getViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val binding = LayoutInflater.from(parent.context).inflate(getLayoutId(), parent, false)
        return getViewHolder(binding)
    }

    fun onBindViewHolder(item: T, viewHolder: RecyclerView.ViewHolder) {
        (viewHolder as ViewHolder<T>).bind(item)
    }

    fun isForViewType(item: AdapterItem): Boolean {
        return item.javaClass == classType
    }
}