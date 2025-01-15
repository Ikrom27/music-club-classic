package ru.ikrom.adapter_delegates.delegates

import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.ikrom.adapter_delegates.R
import ru.ikrom.base_adapter.AdapterItem
import ru.ikrom.base_adapter.DelegateAdapter
import ru.ikrom.theme.AppDrawableIds

data class ArtistHeaderItem(
    val title: String,
    val subtitle: String,
    val thumbnail: String,
    val isFavorite: Boolean
): AdapterItem

class ArtistHeaderDelegate(
    private val onFavoriteClick: () -> Unit,
    private val onShareClick: () -> Unit,
    private val onPlayClick: () -> Unit,
    private val onShuffleClick: () -> Unit
): DelegateAdapter<ArtistHeaderItem, ArtistHeaderDelegate.ThumbnailLargeHeaderViewHolder>(
    ArtistHeaderItem::class.java
) {
    inner class ThumbnailLargeHeaderViewHolder(itemView: View):
    ViewHolder<ArtistHeaderItem>(itemView){
        private val thumbnail: ImageView = itemView.findViewById(R.id.iv_thumbnail)
        private val title: TextView = itemView.findViewById(R.id.tv_title)
        private val btnFavorite: ImageButton = itemView.findViewById(R.id.btn_favorite)
        private val btnShare: ImageButton = itemView.findViewById(R.id.btn_share)
        private val btnPlayAll: Button = itemView.findViewById(R.id.btn_play_all)
        private val btnPlayShuffled: Button = itemView.findViewById(R.id.btn_play_shuffled)
        override fun bind(item: ArtistHeaderItem) {
            Glide
                .with(itemView.context)
                .load(item.thumbnail)
                .centerCrop()
                .into(thumbnail)
            title.text = item.title
            btnFavorite.setImageResource(if(item.isFavorite) AppDrawableIds.FAVORITE else  AppDrawableIds.FAVORITE_BORDERED)
            setupButtons()
        }

        private fun setupButtons() {
            btnPlayAll.setOnClickListener{ onPlayClick() }
            btnPlayShuffled.setOnClickListener{ onShuffleClick() }
            btnFavorite.setOnClickListener{ onFavoriteClick() }
            btnShare.setOnClickListener{ onShareClick() }
        }
    }

    override fun getViewHolder(binding: View): RecyclerView.ViewHolder {
        return ThumbnailLargeHeaderViewHolder(binding)
    }

    override fun getLayoutId(): Int {
        return R.layout.item_artist_header
    }
}