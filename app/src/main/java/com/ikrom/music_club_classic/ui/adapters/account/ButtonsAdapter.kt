package com.ikrom.music_club_classic.ui.adapters.account

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ikrom.music_club_classic.R
import com.ikrom.music_club_classic.ui.adapters.base_adapters.BaseDelegateAdapter
import com.ikrom.music_club_classic.ui.adapters.base_adapters.AdapterItem
import com.ikrom.music_club_classic.ui.components.IconButton

data class ButtonsDelegateItem(
    val buttons: List<IconButton>
): AdapterItem()

class ButtonsAdapter: BaseDelegateAdapter<ButtonsDelegateItem, ButtonsAdapter.ButtonsViewHolder>(
    ButtonsDelegateItem::class.java) {

    inner class ButtonsViewHolder(itemView: View): DelegateViewHolder<ButtonsDelegateItem>(itemView){
        private val container: ViewGroup = itemView.findViewById(R.id.layout_buttons_container)
        override fun bind(item: ButtonsDelegateItem) {
            item.buttons.forEach {
                if (it == item.buttons.lastOrNull()){
                    it.separator.visibility = View.GONE
                } else {
                    it.separator.visibility = View.VISIBLE
                }
                container.addView(it)
            }
        }

    }

    override fun createViewHolder(binding: View): RecyclerView.ViewHolder {
        return ButtonsViewHolder(binding)
    }

    override fun getLayoutId(): Int {
        return R.layout.layout_buttons_group
    }
}