package com.ikrom.music_club_classic.ui.adapters.account

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.ikrom.music_club_classic.R
import com.ikrom.base_adapter.model.AdapterItem

data class HeaderDelegateItem(
    val imageUrl: String,
    val fullName: String
): AdapterItem()

class HeaderAdapter(): com.ikrom.base_adapter.BaseDelegateAdapter<HeaderDelegateItem, HeaderAdapter.HeaderViewHolder>(
    HeaderDelegateItem::class.java
) {
    inner class HeaderViewHolder(itemView: View): DelegateViewHolder <HeaderDelegateItem>(itemView) {
        private val accountImageView: ImageView = itemView.findViewById(R.id.iv_account_image)
        private val nameTextView: TextView = itemView.findViewById(R.id.tv_full_name)

        override fun bind(item: HeaderDelegateItem) {
            Glide
                .with(itemView.context)
                .load(item.imageUrl)
                .placeholder(R.drawable.ic_profile)
                .transform(CircleCrop())
                .into(accountImageView)
            nameTextView.text = item.fullName
        }

    }

    override fun createViewHolder(binding: View): RecyclerView.ViewHolder {
        return HeaderViewHolder(binding)
    }

    override fun getLayoutId(): Int {
        return R.layout.layout_account_header
    }
}