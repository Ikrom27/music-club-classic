package com.ikrom.music_club_classic.ui.screens

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.ikrom.music_club_classic.R
import com.ikrom.music_club_classic.extensions.togglePlayPause
import com.ikrom.music_club_classic.playback.PlayerHandler
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PlayerFragment : Fragment() {
    @Inject
    lateinit var playerHandler: PlayerHandler

    private lateinit var trackCover: ImageView
    private lateinit var trackTitle: TextView
    private lateinit var trackAuthor: TextView
    private lateinit var btnPlayPause: ImageButton
    private lateinit var btnSkipNext: ImageButton
    private lateinit var btnSkipPrevious: ImageButton
    private lateinit var btnToFavorite: ImageButton
    private lateinit var btnToRepeat: ImageButton
    private lateinit var seekBarPlayer: SeekBar

    private val mHandler = Handler(Looper.getMainLooper())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_player, container, false)
        bindViews(view)
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
    }

    private fun setupButtons() {
        playerHandler.isPlaying.observe(viewLifecycleOwner) {
            btnPlayPause.setImageResource(if (it) R.drawable.ic_pause else R.drawable.ic_play)
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
        playerHandler.totalDuration.observe(viewLifecycleOwner) {
            seekBarPlayer.max = it.toInt()
        }
    }

    private fun updateSeekBarPosition(){
        requireActivity().runOnUiThread(object : Runnable {
            override fun run() {
                val position = playerHandler.player.currentPosition.toInt()
                seekBarPlayer.progress = position
                mHandler.postDelayed(this, 100)
            }
        })
    }

    private fun setupButtonsListener() {
        btnPlayPause.setOnClickListener {
            playerHandler.player.togglePlayPause()
        }
        btnSkipNext.setOnClickListener {
            playerHandler.player.seekToNext()
        }
        btnSkipPrevious.setOnClickListener {
            playerHandler.player.seekToPrevious()
        }
        btnToFavorite.setOnClickListener {}
        btnToRepeat.setOnClickListener {}
    }

    private fun setupContent() {
        playerHandler.currentMediaItem.observe(viewLifecycleOwner) {currentItem ->
            if (currentItem != null) {
                Glide
                    .with(requireContext())
                    .load(currentItem.mediaMetadata.artworkUri)
                    .into(trackCover)
                trackTitle.text = currentItem.mediaMetadata.title
                trackAuthor.text = currentItem.mediaMetadata.artist
            }
        }
    }
}