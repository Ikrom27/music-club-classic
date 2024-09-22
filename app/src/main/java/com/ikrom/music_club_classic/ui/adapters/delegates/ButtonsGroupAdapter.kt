package com.ikrom.music_club_classic.ui.adapters.delegates

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ikrom.music_club_classic.R
import com.ikrom.music_club_classic.ui.components.IconButton
import ru.ikrom.ui.base_adapter.DelegateAdapter
import ru.ikrom.ui.base_adapter.model.AdapterItem

data class ButtonsGroupItem(
    val buttons: List<IconButton>
): AdapterItem()

class ButtonsGroupAdapter: DelegateAdapter<ButtonsGroupItem, ButtonsGroupAdapter.ButtonsGroupViewHolder>(
    ButtonsGroupItem::class.java) {

    inner class ButtonsGroupViewHolder(itemView: View): ViewHolder<ButtonsGroupItem>(itemView){
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

    override fun getViewHolder(binding: View): RecyclerView.ViewHolder {
        return ButtonsGroupViewHolder(binding)
    }

    override fun getLayoutId(): Int {
        return R.layout.layout_buttons_group
    }
}