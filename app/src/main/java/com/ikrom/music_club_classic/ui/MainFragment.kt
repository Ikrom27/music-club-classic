package com.ikrom.music_club_classic.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.navigation.NavController
import androidx.navigation.ui.NavigationUI
import com.bumptech.glide.Glide
import androidx.navigation.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.ikrom.music_club_classic.R
import com.ikrom.music_club_classic.extensions.togglePlayPause
import com.ikrom.music_club_classic.playback.PlayerConnection
import com.ikrom.music_club_classic.ui.components.MiniPlayerView
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainFragment : Fragment() {
    @Inject
    lateinit var playerConnection: PlayerConnection
    lateinit var miniPlayerView: MiniPlayerView
    lateinit var navigationView: BottomNavigationView
    var navController: NavController? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_main, container, false)
        miniPlayerView = view.findViewById(R.id.mini_player)
        navigationView = view.findViewById(R.id.bottom_navigation_bar)
        setupMiniPlayer()
        return view
    }

    private fun setupMiniPlayer() {
        miniPlayerView.setOnButtonClickListener { playerConnection.player.togglePlayPause() }
        playerConnection.getCurrentMediaItem().observe(viewLifecycleOwner) {
            if (it != null){
                miniPlayerView.show()
                miniPlayerView.title = it.mediaMetadata.title.toString()
                miniPlayerView.subTitle = it.mediaMetadata.artist.toString()
                Glide
                    .with(this)
                    .load(it.mediaMetadata.artworkUri?.toString())
                    .into(miniPlayerView.getThumbnailImageView())
            }
        }
        playerConnection.isPlaying.observe(viewLifecycleOwner) {
            miniPlayerView.btnIcon = if (it) R.drawable.ic_pause else R.drawable.ic_play
        }
    }

    override fun onStart() {
        super.onStart()
        navController = view?.findViewById<FragmentContainerView>(R.id.nav_host_fragment)
            ?.findNavController()
        NavigationUI.setupWithNavController(navigationView, navController!!)
    }
}