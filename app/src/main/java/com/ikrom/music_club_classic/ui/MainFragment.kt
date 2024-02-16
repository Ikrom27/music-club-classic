package com.ikrom.music_club_classic.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.commit
import androidx.navigation.NavController
import androidx.navigation.ui.NavigationUI
import com.bumptech.glide.Glide
import androidx.navigation.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.ikrom.music_club_classic.R
import com.ikrom.music_club_classic.playback.PlayerHandler
import com.ikrom.music_club_classic.ui.components.MiniPlayerView
import com.ikrom.music_club_classic.ui.screens.PlayerFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainFragment : Fragment() {
    @Inject
    lateinit var playerHandler: PlayerHandler

    private lateinit var miniPlayerView: MiniPlayerView
    private lateinit var navigationView: BottomNavigationView
    private lateinit var behavior: BottomSheetBehavior<FrameLayout>
    private lateinit var slidingView: FrameLayout
    private lateinit var playerContainer: FrameLayout
    private var navController: NavController? = null

    private var NAV_BAR_HEIGHT: Float = 0f
    private var MINI_PLAYER_HEIGHT: Float = 0f
    private var WEINDOW_HEIGHT: Float = 0f

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_main, container, false)
        setupConstants()
        binding(view)
        setupSlidingView()
        return view
    }

    private fun binding(view: View){
        navigationView = view.findViewById(R.id.bottom_navigation_bar)
        miniPlayerView = view.findViewById(R.id.mini_player)
        slidingView = view.findViewById(R.id.layout_sliding_view)
        playerContainer = view.findViewById(R.id.fragment_player_container)
    }

    private fun setupConstants(){
        NAV_BAR_HEIGHT = requireContext().resources.getDimension(R.dimen.bottom_nav_bar_height)
        MINI_PLAYER_HEIGHT = requireContext().resources.getDimension(R.dimen.mini_player_height)
        WEINDOW_HEIGHT = requireContext().resources.displayMetrics.heightPixels.toFloat()
    }

    private fun setupMiniPlayer() {
        bindMiniPlayer()
        setupMiniPlayerButtons()
    }

    private fun bindMiniPlayer(){
        playerHandler.currentMediaItemLiveData.observe(viewLifecycleOwner) {
            if (it != null){
                miniPlayerView.title = it.mediaMetadata.title.toString()
                miniPlayerView.subTitle = it.mediaMetadata.artist.toString()
                Glide
                    .with(this)
                    .load(it.mediaMetadata.artworkUri?.toString())
                    .into(miniPlayerView.getThumbnailImageView())
            }
        }
        playerHandler.isPlayingLiveData.observe(viewLifecycleOwner) {
            miniPlayerView.btnIcon = if (it) R.drawable.ic_pause else R.drawable.ic_play
        }
    }

    private fun setupMiniPlayerButtons(){
        miniPlayerView.setOnButtonClickListener { playerHandler.togglePlayPause() }
        miniPlayerView.setOnLayoutClickListener {behavior.state = BottomSheetBehavior.STATE_EXPANDED}
    }

    private fun setSlidingViewHideAnimation(){
        playerHandler.currentMediaItemLiveData.observe(viewLifecycleOwner) {
            if (it == null){
                slidingView.animate().translationY(WEINDOW_HEIGHT)
            }
            else {
                slidingView.animate().translationY(0F)
            }
        }
    }

    private fun setupSlidingView(){
        setSlidingViewHideAnimation()
        setupBehavior()
        setupMiniPlayer()
        setupPlayerFragment()
    }

    private fun setupPlayerFragment() {
        requireActivity().supportFragmentManager.commit {
            replace(R.id.fragment_player_container, PlayerFragment())
        }
    }

    private fun setupBehavior(){
        behavior = BottomSheetBehavior.from(slidingView)
        behavior.state = BottomSheetBehavior.STATE_COLLAPSED
        behavior.peekHeight = MINI_PLAYER_HEIGHT.toInt() + NAV_BAR_HEIGHT.toInt()
        behavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_COLLAPSED){
                    playerContainer.alpha = 0f
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                miniPlayerAlphaProgress(slideOffset)
                playerContainerAlphaProgress(slideOffset)
                navigationView.translationY = slideOffset * NAV_BAR_HEIGHT
            }
        })
    }

    private fun miniPlayerAlphaProgress(progress: Float){
        val threshold = 0.3f
        if (progress <= threshold) {
            miniPlayerView.alpha = 1f - progress / threshold
        } else {
            miniPlayerView.alpha = 0f
        }
    }

    private fun playerContainerAlphaProgress(progress: Float){
        val threshold = 0.3f
        if (progress >= threshold) {
            val alpha = (progress - threshold) / (1 - threshold)
            playerContainer.alpha = alpha
        } else {
            playerContainer.alpha = 0f
        }
    }

    override fun onStart() {
        super.onStart()
        navController = view?.findViewById<FragmentContainerView>(R.id.nav_host_fragment)
            ?.findNavController()
        NavigationUI.setupWithNavController(navigationView, navController!!)
    }
}