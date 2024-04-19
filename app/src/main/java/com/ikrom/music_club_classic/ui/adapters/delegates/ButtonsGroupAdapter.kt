package com.ikrom.music_club_classic.ui.adapters.delegates

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ikrom.base_adapter.BaseDelegateAdapter
import com.ikrom.base_adapter.model.AdapterItem
import com.ikrom.music_club_classic.R
import com.ikrom.music_club_classic.ui.components.IconButton

data class ButtonsGroupItem(
    val buttons: List<IconButton>
): AdapterItem()

class ButtonsGroupAdapter: BaseDelegateAdapter<ButtonsGroupItem, ButtonsGroupAdapter.ButtonsGroupViewHolder>(
    ButtonsGroupItem::class.java) {

    inner class ButtonsGroupViewHolder(itemView: View): DelegateViewHolder<ButtonsGroupItem>(itemView){
        private val container: ViewGroup = itemView.findViewById(R.id.layout_buttons_container)
        override fun bind(item: ButtonsGroupItem) {
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
        return ButtonsGroupViewHolder(binding)
    }

    override fun getLayoutId(): Int {
        return R.layout.layout_buttons_group
    }
}