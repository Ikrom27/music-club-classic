package com.ikrom.music_club_classic.ui.adapters.delegates

import android.view.View
import android.widget.TextView
import com.ikrom.base_adapter.BaseAdapter
import com.ikrom.base_adapter.model.AdapterItem
import com.ikrom.music_club_classic.R
import java.util.Date

data class SuggestionItem(
    val text: String,
    override val onClick: (() -> Unit)
) : AdapterItem()

class SuggestionAdapter: BaseAdapter<SuggestionItem>() {
    inner class SuggestionViewHolder(itemView: View): BaseViewHolder<SuggestionItem>(itemView) {
        private val tvSuggestionText: TextView = itemView.findViewById(R.id.tv_suggestion_text)
        override fun bind(item: SuggestionItem) {
            tvSuggestionText.text = item.text
        }

    }

    override fun getViewHolder(binding: View): BaseViewHolder<SuggestionItem> {
        return SuggestionViewHolder(binding)
    }

    override fun getLayoutId(): Int {
        return R.layout.item_suggestion
    }
}