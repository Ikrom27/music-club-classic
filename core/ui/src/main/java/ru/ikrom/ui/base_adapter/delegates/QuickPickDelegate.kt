package ru.ikrom.ui.base_adapter.delegates

import android.media.browse.MediaBrowser
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.ikrom.ui.R
import ru.ikrom.youtube_data.model.getNames
import ru.ikrom.ui.base_adapter.DelegateAdapter
import ru.ikrom.youtube_data.model.TrackModel

data class QuickPickItem(
    val track: TrackModel?,
)

class QuickPickDelegate(
    val isPlaying: LiveData<Boolean>,
//    val currentMediaItem: LiveData<MediaItem?>,
    val lifecycleOwner: LifecycleOwner,
    val onPlayPauseClick: (track: TrackModel) -> Unit,
    val onSkipClick: () -> Unit
): DelegateAdapter<QuickPickItem, QuickPickDelegate.QuickPickViewHolder>(
    QuickPickItem::class.java,
) {
    inner class QuickPickViewHolder(itemView: View): ViewHolder<QuickPickItem>(itemView){
        private val trackCover = itemView.findViewById<ImageView>(R.id.iv_track_cover)
        private val trackTitle = itemView.findViewById<TextView>(R.id.tv_title)
        private val trackAuthor = itemView.findViewById<TextView>(R.id.tv_subtitle)
        private val btnPlayPause = itemView.findViewById<ImageButton>(R.id.btn_play_pause)
        private val btnSkip = itemView.findViewById<ImageButton>(R.id.btn_skip)

        override fun bind(item: QuickPickItem) {
            val track = item.track
            Glide
                .with(itemView.context)
                .load(track?.album?.thumbnail)
                .centerCrop()
                .into(trackCover)
            trackTitle.text = track?.title
            trackAuthor.text = track?.album?.artists.getNames()
            setupButtons(track)
            setupPlayPauseIcon(track?.videoId)
        }

        private fun setupButtons(track: TrackModel?){
            btnPlayPause.setOnClickListener {
                track?.let(onPlayPauseClick)
            }
            btnSkip.setOnClickListener {
                onSkipClick()
            }
        }

        private fun setupPlayPauseIcon(trackId: String?){
//            val playPauseState = MediatorLiveData<Pair<Boolean, MediaItem?>>().apply {
//                addSource(isPlaying) { value = it to currentMediaItem.value }
//                addSource(currentMediaItem) { value = isPlaying.value!! to it }
//            }
//            playPauseState.observe(lifecycleOwner) { (isPlaying, mediaItem) ->
//                if (isPlaying && mediaItem?.mediaId == trackId) {
//                    btnPlayPause.setImageResource(R.drawable.ic_pause)
//                } else {
//                    btnPlayPause.setImageResource(R.drawable.ic_play)
//                }
//            }
        }
    }

    override fun getViewHolder(binding: View): RecyclerView.ViewHolder {
        return QuickPickViewHolder(binding)
    }

    override fun getLayoutId(): Int {
        return R.layout.item_player_card
    }
}