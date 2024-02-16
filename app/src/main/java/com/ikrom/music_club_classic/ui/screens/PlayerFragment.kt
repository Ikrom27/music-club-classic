package com.ikrom.music_club_classic.ui.screens

import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.media3.common.Player
import com.bumptech.glide.Glide
import com.google.android.material.slider.Slider
import com.ikrom.music_club_classic.R
import com.ikrom.music_club_classic.extensions.togglePlayPause
import com.ikrom.music_club_classic.playback.PlayerConnection
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PlayerFragment : Fragment() {
    @Inject
    lateinit var playerConnection: PlayerConnection

    private lateinit var trackCover: ImageView
    private lateinit var trackTitle: TextView
    private lateinit var trackAuthor: TextView
    private lateinit var btnPlayPause: ImageButton
    private lateinit var btnSkipNext: ImageButton
    private lateinit var btnSkipPrevious: ImageButton
    private lateinit var btnToFavorite: ImageButton
    private lateinit var btnToRepeat: ImageButton
    private lateinit var seekBarPlayer: SeekBar

    private val mHandler = Handler()

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
        playerConnection.isPlaying.observe(viewLifecycleOwner) {
            btnPlayPause.setImageResource(if (it) R.drawable.ic_pause else R.drawable.ic_play)
        }
    }

    private fun setupSeekBar(){
        seekBarPlayer.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {}

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                playerConnection.player.seekTo(seekBar?.progress!!.toLong())
            }

        })
        playerConnection.totalDuration.observe(viewLifecycleOwner) {
            if (it>0){
                val duration = it
                seekBarPlayer.max = duration.toInt()
            }
        }
        requireActivity().runOnUiThread(object : Runnable {
            override fun run() {
                val position = playerConnection.player.currentPosition.toInt()
                seekBarPlayer.progress = position
                mHandler.postDelayed(this, 100)
            }
        })
    }

    private fun setupButtonsListener() {
        btnPlayPause.setOnClickListener {
            playerConnection.player.togglePlayPause()
        }
        btnSkipNext.setOnClickListener {
            playerConnection.player.seekToNext()
        }
        btnSkipPrevious.setOnClickListener {
            playerConnection.player.seekToPrevious()
        }
        btnToFavorite.setOnClickListener {}

        btnToRepeat.setOnClickListener {}
    }

    private fun setupContent() {
        playerConnection.getCurrentMediaItem().observe(viewLifecycleOwner) {mediaItem ->
            if (mediaItem != null) {
                Glide
                    .with(requireContext())
                    .load(mediaItem.mediaMetadata.artworkUri)
                    .into(trackCover)
                trackTitle.text = mediaItem.mediaMetadata.title
                trackAuthor.text = mediaItem.mediaMetadata.artist
            }
        }
    }
}