package ru.ikrom.adapter_delegates.delegates

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import ru.ikrom.adapter_delegates.R
import ru.ikrom.base_adapter.AdapterItem
import ru.ikrom.theme.AppColorIds
import ru.ikrom.base_adapter.DelegateAdapter
import ru.ikrom.utils.ColorsUtil

data class ThumbnailHeaderItem(
    val title: String,
    val subtitle: String,
    val thumbnail: String,
): AdapterItem

class ThumbnailHeaderDelegate(
    val onPlayClick: () -> Unit,
    val onShuffleClick: () -> Unit,
    val onArtistClick: (() -> Unit)? = null
): DelegateAdapter<ThumbnailHeaderItem, ThumbnailHeaderDelegate.ThumbnailViewHolder>(
    ThumbnailHeaderItem::class.java
    ) {
    inner class ThumbnailViewHolder(itemView: View)
        : ViewHolder<ThumbnailHeaderItem>(itemView) {
        private val container: View = itemView.findViewById(R.id.container)
        private val thumbnail: ImageView = itemView.findViewById(R.id.iv_thumbnail_view)
        private val title: TextView = itemView.findViewById(R.id.tv_title)
        private val author: TextView = itemView.findViewById(R.id.tv_author)
        private val btnPlayAll: Button = itemView.findViewById(R.id.btn_play_all)
        private val btnPlayShuffled: Button = itemView.findViewById(R.id.btn_play_shuffled)

        override fun bind(item: ThumbnailHeaderItem) {
            Glide
                .with(itemView.context)
                .load(item.thumbnail)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>,
                        isFirstResource: Boolean
                    ): Boolean {
                        Toast.makeText(itemView.context, "Fail to load image", Toast.LENGTH_SHORT).show()
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable,
                        model: Any,
                        target: Target<Drawable>?,
                        dataSource: DataSource,
                        isFirstResource: Boolean
                    ): Boolean {
                        setupBackground(itemView.context, resource)
                        return false
                    }

                })
                .into(thumbnail)
            title.text = item.title
            author.text = item.subtitle
            setupButtons(item)
        }

        private fun setupButtons(item: ThumbnailHeaderItem) {
            btnPlayAll.setOnClickListener{
                onPlayClick()
            }
            btnPlayShuffled.setOnClickListener{
                onShuffleClick()
            }
            author.setOnClickListener {
                onArtistClick?.let { it1 -> it1() }
            }
        }

        private fun setupBackground(context: Context ,resource: Drawable) {
            val bitmap = (resource as BitmapDrawable).bitmap

            Palette.from(bitmap).generate { palette ->
                val dominantColor = ColorsUtil.adjustColorToBackground(palette?.getDominantColor(Color.BLACK) ?: Color.BLACK)
                val background = ContextCompat.getColor(context, AppColorIds.BACKGROUND)
                val gradientDrawable = GradientDrawable(
                    GradientDrawable.Orientation.TOP_BOTTOM,
                    intArrayOf(dominantColor, background)
                )
                container.background = gradientDrawable
            }
        }
    }

    override fun getViewHolder(binding: View): RecyclerView.ViewHolder {
        return ThumbnailViewHolder(binding)
    }

    override fun getLayoutId(): Int {
        return R.layout.item_thumbnail_header
    }

}