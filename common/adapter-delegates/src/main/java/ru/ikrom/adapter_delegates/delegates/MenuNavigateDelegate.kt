package ru.ikrom.adapter_delegates.delegates

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.ikrom.adapter_delegates.R
import ru.ikrom.base_adapter.AdapterItem
import ru.ikrom.base_adapter.DelegateAdapter

data class MenuNavigateItem(
    val iconId: Int,
    val title: String,
): AdapterItem


class MenuNavigateDelegate(
    onClickItem: (MenuNavigateItem) -> Unit,
): DelegateAdapter<MenuNavigateItem, MenuNavigateDelegate.MenuNavigateViewHolder>(
    MenuNavigateItem::class.java, onClickItem) {
    inner class MenuNavigateViewHolder(itemView: View): ViewHolder<MenuNavigateItem>(itemView) {
        private val ivIcon: ImageView = itemView.findViewById(R.id.iv_icon)
        private val tvTitle: TextView = itemView.findViewById(R.id.tv_title)

        override fun bind(item: MenuNavigateItem) {
            ivIcon.setImageResource(item.iconId)
            tvTitle.text = item.title
        }
    }

    override fun getViewHolder(binding: View): RecyclerView.ViewHolder {
        return MenuNavigateViewHolder(binding)
    }

    override fun getLayoutId(): Int {
        return R.layout.item_menu_navigate
    }
}