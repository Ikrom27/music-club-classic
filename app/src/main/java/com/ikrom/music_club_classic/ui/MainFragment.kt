package com.ikrom.music_club_classic.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.navigation.NavController
import androidx.navigation.ui.NavigationUI
import com.bumptech.glide.Glide
import androidx.navigation.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetBehavior
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

    private lateinit var miniPlayerView: MiniPlayerView
    private lateinit var navigationView: BottomNavigationView
    private lateinit var behavior: BottomSheetBehavior<FrameLayout>
    private lateinit var slidingView: FrameLayout
    private var navController: NavController? = null
    private var navBarHeight: Float = 0f
    private var miniPlayerHeight: Float = 0f

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_main, container, false)
        navigationView = view.findViewById(R.id.bottom_navigation_bar)
        miniPlayerView = view.findViewById(R.id.mini_player)
        slidingView = view.findViewById(R.id.layout_sliding_view)
        navBarHeight = requireActivity().resources.getDimension(R.dimen.bottom_nav_bar_height)
        miniPlayerHeight = requireActivity().resources.getDimension(R.dimen.mini_player_height)
        setupMiniPlayer()
        setupSlidingView()
        return view
    }

    private fun setupMiniPlayer() {
        bindMiniPlayer()
        setupMiniPlayerButtons()
    }

    private fun bindMiniPlayer(){
        playerConnection.getCurrentMediaItem().observe(viewLifecycleOwner) {
            if (it != null){
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

    private fun setupMiniPlayerButtons(){
        miniPlayerView.setOnButtonClickListener { playerConnection.player.togglePlayPause() }
        miniPlayerView.setOnLayoutClickListener {behavior.state = BottomSheetBehavior.STATE_EXPANDED}
    }

    private fun setupSlidingView(){
        setupBehavior()
    }

    private fun setupBehavior(){
        behavior = BottomSheetBehavior.from(slidingView)
        behavior.state = BottomSheetBehavior.STATE_COLLAPSED
        behavior.peekHeight = miniPlayerHeight.toInt() + navBarHeight.toInt()
        behavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {}

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                hidingPreviewProgress(slideOffset)
                navigationView.translationY = slideOffset * navBarHeight
            }

        })
    }

    private fun hidingPreviewProgress(progress: Float){
        miniPlayerView.alpha = 1 - progress
    }

    override fun onStart() {
        super.onStart()
        navController = view?.findViewById<FragmentContainerView>(R.id.nav_host_fragment)
            ?.findNavController()
        NavigationUI.setupWithNavController(navigationView, navController!!)
    }
}