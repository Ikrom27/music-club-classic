package ru.ikrom.ui.base_adapter.delegates

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import ru.ikrom.theme.AppIconsId
import ru.ikrom.ui.R
import ru.ikrom.ui.base_adapter.DelegateAdapter

data class AccountHeaderItem(
    val imageUrl: String,
    val fullName: String
)

class AccountHeaderAdapter: DelegateAdapter<AccountHeaderItem, AccountHeaderAdapter.AccountHeaderViewHolder>(
    AccountHeaderItem::class.java
) {
    inner class AccountHeaderViewHolder(itemView: View): ViewHolder<AccountHeaderItem>(itemView) {
        private val ivAccountPicture: ImageView = itemView.findViewById(R.id.iv_account_image)
        private val tvName: TextView = itemView.findViewById(R.id.tv_full_name)

        override fun bind(item: AccountHeaderItem) {
            Glide
                .with(itemView.context)
                .load(item.imageUrl)
                .placeholder(AppIconsId.profile)
                .transform(CircleCrop())
                .into(ivAccountPicture)
            tvName.text = item.fullName
        }

    }

    override fun getViewHolder(binding: View): RecyclerView.ViewHolder {
        return AccountHeaderViewHolder(binding)
    }

    override fun getLayoutId(): Int {
        return R.layout.item_account_header
    }
}