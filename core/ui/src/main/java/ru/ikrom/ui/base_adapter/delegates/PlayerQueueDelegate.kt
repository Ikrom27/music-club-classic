package ru.ikrom.ui.base_adapter.delegates

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import es.claucookie.miniequalizerlibrary.EqualizerView
import ru.ikrom.ui.R
import ru.ikrom.ui.base_adapter.AdapterItem
import ru.ikrom.ui.base_adapter.DelegateAdapter

data class PlayerQueueItem(
    val id: String,
    val title: String,
    val subtitle: String,
    val thumbnail: String,
    val isPlaying: Boolean = false
): AdapterItem()

class PlayerQueueDelegate(
    onClickItem: (PlayerQueueItem) -> Unit,
    onLongClickItem: (PlayerQueueItem) -> Unit = {},
) :
    DelegateAdapter<PlayerQueueItem, PlayerQueueDelegate.PlayerQueueViewHolder>(
    PlayerQueueItem::class.java, onClickItem, onLongClickItem){
        var currentTrackId = ""

    inner class PlayerQueueViewHolder(itemView: View): ViewHolder<PlayerQueueItem>(itemView){
        private val container: View = itemView.findViewById(R.id.container)
        private val cover: ImageView = itemView.findViewById(R.id.iv_thumbnail)
        private val titleTextView: TextView = itemView.findViewById(R.id.tv_title)
        private val subtitleTextView: TextView = itemView.findViewById(R.id.tv_subtitle)
        private val equalizerView: EqualizerView = itemView.findViewById(R.id.equalizer_view)
        private val equalizerShadow: View = itemView.findViewById(R.id.equalizer_view_shadow)

        override fun bind(item: PlayerQueueItem) {
            Glide
                .with(itemView.context)
                .load(item.thumbnail)
                .into(cover)
            titleTextView.text = item.title
            subtitleTextView.text= item.subtitle
            if (item.id == currentTrackId){
                equalizerView.visibility = View.VISIBLE
                equalizerShadow.visibility = View.VISIBLE
                equalizerView.animateBars()
                container.requestFocus()
            } else {
                equalizerView.visibility = View.GONE
                equalizerShadow.visibility = View.GONE
                container.clearFocus()
            }
        }
    }

    override fun getViewHolder(binding: View): RecyclerView.ViewHolder {
        return PlayerQueueViewHolder(binding)
    }

    override fun getLayoutId(): Int {
        return R.layout.item_player_queue
    }
}