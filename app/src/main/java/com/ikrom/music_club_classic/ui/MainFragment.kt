package com.ikrom.music_club_classic.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.ikrom.music_club_classic.R
import com.ikrom.music_club_classic.anim.miniPlayerAlphaProgress
import com.ikrom.music_club_classic.anim.playerContainerAlphaProgress
import com.ikrom.music_club_classic.ui.components.MiniPlayerView
import com.ikrom.music_club_classic.ui.screens.PlayerFragment
import com.ikrom.music_club_classic.utils.setupMarginFromStatusBar
import com.ikrom.music_club_classic.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import ru.ikrom.theme.AppDimens
import ru.ikrom.theme.AppDimens.toDp
import ru.ikrom.theme.AppIconsId

@AndroidEntryPoint
class MainFragment: Fragment() {
    val viewModel: MainViewModel by viewModels()

    private lateinit var miniPlayerView: MiniPlayerView
    private lateinit var navigationView: BottomNavigationView
    private lateinit var navHostFragment: FragmentContainerView
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
        setupMarginFromStatusBar(navHostFragment)
        if (viewModel.bottomSheetState == BottomSheetBehavior.STATE_EXPANDED){
            miniPlayerView.alpha = 0f
            playerContainer.alpha = 1f
            navigationView.translationY = NAV_BAR_HEIGHT
        }
        return view
    }

    private fun binding(view: View){
        navigationView = view.findViewById(R.id.bottom_navigation_bar)
        miniPlayerView = view.findViewById(R.id.mini_player)
        slidingView = view.findViewById(R.id.layout_sliding_view)
        playerContainer = view.findViewById(R.id.fragment_player_container)
        navHostFragment = view.findViewById(R.id.nav_host_fragment)
    }

    private fun setupConstants(){
        NAV_BAR_HEIGHT = AppDimens.bottomNavBarHeight.toDp(requireContext())
        MINI_PLAYER_HEIGHT = AppDimens.miniPlayerHeight.toDp(requireContext())
        WEINDOW_HEIGHT = requireContext().resources.displayMetrics.heightPixels.toFloat()
    }

    private fun setupMiniPlayer() {
        bindMiniPlayer()
        setupMiniPlayerButtons()
    }

    private fun bindMiniPlayer(){
        viewModel.currentMediaItem.observe(viewLifecycleOwner) {
            if (it != null){
                miniPlayerView.title = it.mediaMetadata.title.toString()
                miniPlayerView.subTitle = it.mediaMetadata.artist.toString()
                miniPlayerView.thumbnailUrl = it.mediaMetadata.artworkUri?.toString() ?: ""
            }
        }
        viewModel.isPlaying.observe(viewLifecycleOwner) {
            miniPlayerView.btnIcon = if (it) AppIconsId.pause else AppIconsId.play
        }
    }

    private fun setupMiniPlayerButtons(){
        miniPlayerView.setOnButtonClickListener { viewModel.togglePlayPause() }
        miniPlayerView.setOnLayoutClickListener {behavior.state = BottomSheetBehavior.STATE_EXPANDED}
    }

    private fun setSlidingViewHideAnimation(){
        viewModel.currentMediaItem.observe(requireActivity()) {
            if (it == null){
                slidingView.animate().translationY(WEINDOW_HEIGHT)
                slidingView.visibility = View.GONE
            }
            else {
                slidingView.visibility = View.VISIBLE
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
        behavior.state = viewModel.bottomSheetState
        behavior.peekHeight = MINI_PLAYER_HEIGHT.toInt() + NAV_BAR_HEIGHT.toInt()
        behavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_COLLAPSED){
                    playerContainer.alpha = 0f
                }
                viewModel.bottomSheetState = newState
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                miniPlayerAlphaProgress(miniPlayerView, slideOffset)
                playerContainerAlphaProgress(playerContainer, slideOffset)
                navigationView.translationY = slideOffset * NAV_BAR_HEIGHT
            }
        })
    }

    override fun onStart() {
        super.onStart()
        navController = view?.findViewById<FragmentContainerView>(R.id.nav_host_fragment)
            ?.findNavController()
        NavigationUI.setupWithNavController(navigationView, navController!!)
    }
}