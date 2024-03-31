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
import androidx.core.graphics.ColorUtils
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
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
import com.ikrom.music_club_classic.utils.setupMarginFromStatusBar
import com.ikrom.music_club_classic.viewmodel.PlayerViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PlayerFragment : Fragment() {
    @Inject
    lateinit var playerHandler: PlayerHandler

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

    private val mHandler = Handler(Looper.getMainLooper())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val isPortrait = resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT
        Log.d("PLAYER", if (isPortrait) "portret" else "land")
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

    private fun bindViews(view: View){
        trackCover = view.findViewById(R.id.iv_track_cover)
        trackTitle = view.findViewById(R.id.tv_track_title)
        trackAuthor = view.findViewById(R.id.tv_track_author)
        btnPlayPause = view.findViewById(R.id.ib_play_pause)
        btnSkipNext = view.findViewById(R.id.ib_skip_next)
        btnSkipPrevious = view.findViewById(R.id.ib_skip_previous)
        btnToFavorite = view.findViewById(R.id.ib_to_favorite)
        btnToRepeat = view.findViewById(R.id.ib_to_repeat)
        seekBarPlayer = view.findViewById(R.id.slider_player_progress)
        totalTime = view.findViewById(R.id.tv_total_time)
        progressTime = view.findViewById(R.id.tv_progress_time)
        container = view.findViewById(R.id.container)
        handle = view.findViewById(R.id.handle)
    }

    private fun setupButtons() {
        playerHandler.isPlayingLiveData.observe(viewLifecycleOwner) {
            btnPlayPause.setImageResource(if (it) R.drawable.ic_pause else R.drawable.ic_play)
        }
        playerHandler.repeatModeLiveData.observe(viewLifecycleOwner) {repeatMode ->
            when(repeatMode) {
                Player.REPEAT_MODE_OFF -> btnToRepeat.setImageResource(R.drawable.ic_repeat_off)
                Player.REPEAT_MODE_ONE -> btnToRepeat.setImageResource(R.drawable.ic_repeat_one)
                Player.REPEAT_MODE_ALL -> btnToRepeat.setImageResource(R.drawable.ic_repeat_all)
            }
        }
        playerHandler.currentMediaItemLiveData.observe(viewLifecycleOwner) {mediaItem ->
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
                playerHandler.player.seekTo(seekBar?.progress!!.toLong())
            }
        })
        setSeekbarMaxValue()
        updateSeekBarPosition()
    }

    private fun setSeekbarMaxValue(){
        playerHandler.totalDurationLiveData.observe(viewLifecycleOwner) {
            seekBarPlayer.max = it.toInt()
            totalTime.text = it.toTimeString()
        }
    }

    private fun updateSeekBarPosition(){
        requireActivity().runOnUiThread(object : Runnable {
            override fun run() {
                val position = playerHandler.player.currentPosition
                seekBarPlayer.progress = position.toInt()
                progressTime.text = position.toTimeString()
                mHandler.postDelayed(this, 100)
            }
        })
    }

    private fun setupButtonsListener() {
        btnPlayPause.setOnClickListener {
            playerHandler.togglePlayPause()
        }
        btnSkipNext.setOnClickListener {
            playerHandler.player.seekToNext()
        }
        btnSkipPrevious.setOnClickListener {
            playerHandler.player.seekToPrevious()
        }
        btnToRepeat.setOnClickListener {
            playerHandler.toggleRepeat()
        }
        btnToFavorite.setOnClickListener {
            playerViewModel.toggleToFavorite(playerHandler.currentMediaItemLiveData.value!!)
        }
    }

    private fun setupBackground(resource: Drawable) {
        val bitmap = (resource as BitmapDrawable).bitmap

        Palette.from(bitmap).generate { palette ->
            val dominantColor = darkenColor(palette?.getDominantColor(Color.BLACK) ?: Color.BLACK)
            val mutedColor = darkenColor(palette?.getMutedColor( Color.WHITE) ?: Color.WHITE)
            val gradientDrawable = GradientDrawable(
                GradientDrawable.Orientation.TL_BR,
                intArrayOf(mutedColor, dominantColor)
            )
            container.background = gradientDrawable
        }
    }

    private fun darkenColor(color: Int): Int {
        val hsv = FloatArray(3)
        Color.colorToHSV(color, hsv)
        val luminance = ColorUtils.calculateLuminance(color)
        if (luminance > 0.4){
            hsv[2] *= 0.3f
        } else {
            hsv[2] *= 0.8f
        }
        if (hsv[1] > 0.5){
            hsv[1] *= 0.4f
        }
        return Color.HSVToColor(hsv)
    }

    private fun setupContent() {
        playerHandler.currentMediaItemLiveData.observe(viewLifecycleOwner) { currentItem ->
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