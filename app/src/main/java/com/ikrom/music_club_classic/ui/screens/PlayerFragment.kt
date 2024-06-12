package com.ikrom.music_club_classic.ui.screens

import android.content.res.Configuration
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.media3.common.Player
import androidx.palette.graphics.Palette
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.ikrom.music_club_classic.R
import com.ikrom.music_club_classic.extensions.toTimeString
import com.ikrom.music_club_classic.playback.PlayerHandler
import com.ikrom.music_club_classic.utils.ColorsUtil
import com.ikrom.music_club_classic.utils.setupMarginFromStatusBar
import com.ikrom.music_club_classic.viewmodel.PlayerViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PlayerFragment : Fragment() {
    private val playerViewModel: PlayerViewModel by viewModels()
    private lateinit var container: ConstraintLayout
    private lateinit var trackCover: ImageView
    private lateinit var trackTitle: TextView
    private lateinit var trackAuthor: TextView
    private lateinit var btnPlayPause: ImageButton
    private lateinit var btnSkipNext: ImageButton
    private lateinit var btnSkipPrevious: ImageButton
    private lateinit var btnToFavorite: ImageButton
    private lateinit var btnToRepeat: ImageButton
    private lateinit var seekBarPlayer: SeekBar
    private lateinit var progressTime: TextView
    private lateinit var totalTime: TextView
    private lateinit var handle: View
    private lateinit var btnCast: ImageButton
    private lateinit var btnQueue: ImageButton
    private lateinit var btnCaptions: ImageButton

    private val mHandler = Handler(Looper.getMainLooper())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val isPortrait = resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT
        val view = inflater.inflate(
            if (isPortrait) R.layout.fragment_player else R.layout.fragment_player_horizontal, container, false)
        bindViews(view)
        setupMarginFromStatusBar(handle)
        setupContent()
        setupButtons()
        setupButtonsListener()
        setupSeekBar()
        return view
    }

    private fun bindViews(view: View) {
        trackCover = view.findViewById(R.id.iv_track_cover) ?: ImageView(view.context)
        trackTitle = view.findViewById(R.id.tv_track_title) ?: TextView(view.context)
        trackAuthor = view.findViewById(R.id.tv_track_author) ?: TextView(view.context)
        btnPlayPause = view.findViewById(R.id.ib_play_pause) ?: ImageButton(view.context)
        btnSkipNext = view.findViewById(R.id.ib_skip_next) ?: ImageButton(view.context)
        btnSkipPrevious = view.findViewById(R.id.ib_skip_previous) ?: ImageButton(view.context)
        btnToFavorite = view.findViewById(R.id.ib_to_favorite) ?: ImageButton(view.context)
        btnToRepeat = view.findViewById(R.id.ib_to_repeat) ?: ImageButton(view.context)
        seekBarPlayer = view.findViewById(R.id.slider_player_progress) ?: SeekBar(view.context)
        totalTime = view.findViewById(R.id.tv_total_time) ?: TextView(view.context)
        progressTime = view.findViewById(R.id.tv_progress_time) ?: TextView(view.context)
        container = view.findViewById(R.id.container) ?: ConstraintLayout(view.context)
        handle = view.findViewById(R.id.handle) ?: View(view.context)
        btnCast = view.findViewById(R.id.ib_cast) ?: ImageButton(view.context)
        btnQueue = view.findViewById(R.id.ib_queue) ?: ImageButton(view.context)
        btnCaptions = view.findViewById(R.id.ib_captions) ?: ImageButton(view.context)
    }

    private fun setupButtons() {
        playerViewModel.isPlayingLiveData.observe(viewLifecycleOwner){
            btnPlayPause.setImageResource(if (it) R.drawable.ic_pause else R.drawable.ic_play)
        }
        playerViewModel.repeatModeLiveData.observe(viewLifecycleOwner){repeatMode ->
            when(repeatMode) {
                Player.REPEAT_MODE_OFF -> btnToRepeat.setImageResource(R.drawable.ic_repeat_off)
                Player.REPEAT_MODE_ONE -> btnToRepeat.setImageResource(R.drawable.ic_repeat_one)
                Player.REPEAT_MODE_ALL -> btnToRepeat.setImageResource(R.drawable.ic_repeat_all)
            }
        }
        playerViewModel.currentMediaItemLiveData.observe(viewLifecycleOwner){mediaItem ->
            if (mediaItem != null) {
                playerViewModel.isFavorite(mediaItem.mediaId).observe(viewLifecycleOwner) {
                    btnToFavorite.setImageResource(if (it) R.drawable.ic_favorite else R.drawable.ic_favorite_bordered)
                }
            }
        }
    }

    private fun setupSeekBar(){
        seekBarPlayer.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {}

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                playerViewModel.seekTo(seekBarPlayer.progress.toLong())
            }
        })
        setSeekbarMaxValue()
        updateSeekBarPosition()
    }

    private fun setSeekbarMaxValue(){
        playerViewModel.totalDurationLiveData.observe(viewLifecycleOwner) {
            seekBarPlayer.max = it.toInt()
            totalTime.text = it.toTimeString()
        }
    }

    private fun updateSeekBarPosition(){
        requireActivity().runOnUiThread(object : Runnable {
            override fun run() {
                val position = playerViewModel.currentPositionLiveData
                seekBarPlayer.progress = position.toInt()
                progressTime.text = position.toTimeString()
                mHandler.postDelayed(this, 100)
            }
        })
    }

    private fun setupButtonsListener() {
        btnPlayPause.setOnClickListener {
            playerViewModel.togglePlayPause()
        }
        btnSkipNext.setOnClickListener {
            playerViewModel.seekToNext()
        }
        btnSkipPrevious.setOnClickListener {
            playerViewModel.seekToPrevious()
        }
        btnToRepeat.setOnClickListener {
            playerViewModel.toggleRepeat()
        }
        btnToFavorite.setOnClickListener {
            // TODO: Not yet implemented
            Toast.makeText(requireContext(), "Not yet implemented", Toast.LENGTH_SHORT).show()
        }
        btnCast.setOnClickListener {
            // TODO: Not yet implemented
            Toast.makeText(requireContext(), "Not yet implemented", Toast.LENGTH_SHORT).show()
        }
        btnQueue.setOnClickListener {
            val bottomQueueFragment = PlayerQueueFragment()
            bottomQueueFragment.show(parentFragmentManager, bottomQueueFragment.tag)
        }
        btnCaptions.setOnClickListener {
            // TODO: Not yet implemented
            Toast.makeText(requireContext(), "Not yet implemented", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupBackground(resource: Drawable) {
        val bitmap = (resource as BitmapDrawable).bitmap

        Palette.from(bitmap).generate { palette ->
            val dominantColor = ColorsUtil.adjustColorToBackground(palette?.getDominantColor(Color.BLACK) ?: Color.BLACK, true)
            val mutedColor = ColorsUtil.adjustColorToBackground(palette?.getMutedColor( Color.WHITE) ?: Color.WHITE, true)
            val gradientDrawable = GradientDrawable(
                GradientDrawable.Orientation.TL_BR,
                intArrayOf(mutedColor, dominantColor)
            )
            container.background = gradientDrawable
        }
    }

    private fun setupContent() {
        playerViewModel.currentMediaItemLiveData.observe(viewLifecycleOwner) { currentItem ->
            if (currentItem != null) {
                Glide
                    .with(requireContext())
                    .load(currentItem.mediaMetadata.artworkUri)
                    .centerCrop()
                    .listener(object : RequestListener<Drawable> {
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Drawable>,
                            isFirstResource: Boolean
                        ): Boolean {
                            Toast.makeText(requireContext(), "Fail to load image", Toast.LENGTH_SHORT).show()
                            return false
                        }

                        override fun onResourceReady(
                            resource: Drawable,
                            model: Any,
                            target: Target<Drawable>?,
                            dataSource: DataSource,
                            isFirstResource: Boolean
                        ): Boolean {
                            setupBackground(resource)
                            return false
                        }

                    })
                    .into(trackCover)
                trackTitle.text = currentItem.mediaMetadata.title
                trackAuthor.text = currentItem.mediaMetadata.artist
            }
        }
    }
}