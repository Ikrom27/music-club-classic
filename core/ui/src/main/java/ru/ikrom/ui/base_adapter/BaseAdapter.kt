package ru.ikrom.ui.base_adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.ikrom.ui.base_adapter.model.AdapterItem

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
        val item = mItems[position]
        holder.bind(item)
        super.onBindViewHolder(holder, position)
    }
}