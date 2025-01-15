package ru.ikrom.ui.base_adapter.delegates

import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.ikrom.theme.AppDrawableIds
import ru.ikrom.ui.R
import ru.ikrom.ui.base_adapter.DelegateAdapter

data class MenuHeaderDelegateItem(
    override val id: String,
    override val title: String,
    override val subtitle: String,
    override val thumbnail: String,
    val isFavorite: Boolean
): ThumbnailItem

class MenuHeaderDelegate(
    val onClick: (MenuHeaderDelegateItem) -> Unit,
    val onFavoriteClick: () -> Unit
): DelegateAdapter<MenuHeaderDelegateItem, MenuHeaderDelegate.MenuHeaderViewHolder>(
    MenuHeaderDelegateItem::class.java, onClick) {
    inner class MenuHeaderViewHolder(itemView: View): ViewHolder<MenuHeaderDelegateItem>(itemView){
        private val tvTitle: TextView = itemView.findViewById(R.id.tv_title)
        private val tvSubtitle: TextView = itemView.findViewById(R.id.tv_subtitle)
        private val ivThumbnail: ImageView = itemView.findViewById(R.id.iv_thumbnail)
        private val ibFavorite: ImageButton = itemView.findViewById(R.id.ic_favorite)

        override fun bind(item: MenuHeaderDelegateItem) {
            tvTitle.text = item.title
            tvSubtitle.text = item.subtitle
            Glide
                .with(itemView.context)
                .load(item.thumbnail)
                .centerCrop()
                .into(ivThumbnail)
            ibFavorite.setOnClickListener {
                onFavoriteClick()
            }
            ibFavorite.setImageResource(
                if(item.isFavorite) AppDrawableIds.FAVORITE
                else AppDrawableIds.FAVORITE_BORDERED
            )
            if(item.subtitle.isBlank()){
                tvSubtitle.visibility = View.GONE
            }
        }

    }

    override fun getViewHolder(binding: View): RecyclerView.ViewHolder {
        return MenuHeaderViewHolder(binding)
    }

    override fun getLayoutId(): Int {
        return R.layout.item_menu_header
    }

}