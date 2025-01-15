package ru.ikrom.adapter_delegates.delegates

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.ikrom.adapter_delegates.R
import ru.ikrom.base_adapter.AdapterItem
import ru.ikrom.base_adapter.DelegateAdapter

data class MenuButtonItem(
    val title: String,
    val icon: Int,
    val onClick: () -> Unit
): AdapterItem

class MenuButtonDelegate(
    val onClick: (MenuButtonItem) -> Unit
): DelegateAdapter<MenuButtonItem, MenuButtonDelegate.MenuButtonViewHolder>(
    MenuButtonItem::class.java, onClick
) {
    inner class MenuButtonViewHolder(itemView: View) : ViewHolder<MenuButtonItem>(itemView){
        private val container: View = itemView.findViewById(R.id.container)
        private val ivIcon: ImageView = itemView.findViewById(R.id.iv_icon)
        private val tvTitle: TextView = itemView.findViewById(R.id.tv_title)

        override fun bind(item: MenuButtonItem) {
            container.setOnClickListener { onClick(item) }
            ivIcon.setImageResource(item.icon)
            tvTitle.text = item.title
        }

    }

    override fun getViewHolder(binding: View): RecyclerView.ViewHolder {
        return MenuButtonViewHolder(binding)
    }

    override fun getLayoutId(): Int {
        return R.layout.item_menu_button
    }
}