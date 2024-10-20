package com.ikrom.music_club_classic

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.FrameLayout
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.commit
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import dagger.hilt.android.AndroidEntryPoint
import ru.ikrom.mini_player.MiniPlayer
import ru.ikrom.player.MusicPlayerService
import ru.ikrom.player_screen.PlayerFragment
import ru.ikrom.theme.AppDimens
import ru.ikrom.theme.AppDimens.toDp
import ru.ikrom.theme.AppIconsId
import ru.ikrom.utils.setupMarginFromStatusBar


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()

    private lateinit var miniPlayer: MiniPlayer
    private lateinit var navigationView: BottomNavigationView
    private lateinit var navHostFragment: FragmentContainerView
    private lateinit var behavior: BottomSheetBehavior<FrameLayout>
    private lateinit var slidingView: FrameLayout
    private lateinit var playerContainer: FrameLayout

    private val navBarHeight by lazy { AppDimens.bottomNavBarHeight.toDp(this) }
    private val miniPlayerHeight by lazy { AppDimens.miniPlayerHeight.toDp(this) }
    private val windowHeight by lazy { resources.displayMetrics.heightPixels.toFloat() }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupPlayerService()

        bindViews()
        setupSlidingView()
        setupMarginFromStatusBar(navHostFragment)
        restoreBottomSheetState()
    }

    override fun onStart() {
        super.onStart()
        NavigationUI.setupWithNavController(navigationView, navHostFragment.findNavController())
    }

    override fun onDestroy() {
        super.onDestroy()
        if (!isChangingConfigurations) {
            destroyService()
        }
    }

    override fun onResume() {
        super.onResume()
        disableInputAdjustment()
    }

    private fun destroyService(){
        val intent = Intent(this, MusicPlayerService::class.java)
        stopService(intent)
    }

    private fun setupPlayerService(){
        val intent = Intent(this, MusicPlayerService::class.java)
        startForegroundService(intent)
    }

    private fun disableInputAdjustment(){
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
    }

    private fun bindViews() {
        navigationView = findViewById(R.id.bottom_navigation_bar)
        miniPlayer = findViewById(R.id.mini_player)
        slidingView = findViewById(R.id.layout_sliding_view)
        playerContainer = findViewById(R.id.fragment_player_container)
        navHostFragment = findViewById(R.id.nav_host_fragment)
    }

    private fun restoreBottomSheetState() {
        if (viewModel.bottomSheetState == BottomSheetBehavior.STATE_EXPANDED) {
            miniPlayer.alpha = 0f
            playerContainer.alpha = 1f
            navigationView.translationY = navBarHeight
        }
    }

    private fun setupSlidingView() {
        setSlidingViewHideAnimation()
        setupBehavior()
        setupMiniPlayer()
        setupPlayerFragment()
    }

    private fun setSlidingViewHideAnimation() {
        viewModel.currentMediaItem.observe(this) { mediaItem ->
            if (mediaItem == null) {
                slidingView.animate().translationY(windowHeight).withEndAction {
                    slidingView.visibility = View.GONE
                }
            } else {
                slidingView.visibility = View.VISIBLE
                slidingView.animate().translationY(0F)
            }
        }
    }

    private fun setupMiniPlayer() {
        viewModel.currentMediaItem.observe(this) { mediaItem ->
            mediaItem?.let {
                miniPlayer.apply {
                    title = it.mediaMetadata.title.toString()
                    subTitle = it.mediaMetadata.artist.toString()
                    thumbnailUrl = it.mediaMetadata.artworkUri?.toString() ?: ""
                }
            }
        }

        viewModel.isPlaying.observe(this) { isPlaying ->
            miniPlayer.btnIcon = if (isPlaying) AppIconsId.pause else AppIconsId.play
        }

        miniPlayer.setOnButtonClickListener { viewModel.togglePlayPause() }
        miniPlayer.setOnLayoutClickListener { behavior.state = BottomSheetBehavior.STATE_EXPANDED }
    }

    private fun setupPlayerFragment() {
        supportFragmentManager.commit {
            replace(R.id.fragment_player_container, PlayerFragment())
        }
    }

    private fun setupBehavior() {
        behavior = BottomSheetBehavior.from(slidingView).apply {
            state = viewModel.bottomSheetState
            peekHeight = (miniPlayerHeight + navBarHeight).toInt()
            addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                        playerContainer.alpha = 0f
                    }
                    viewModel.bottomSheetState = newState
                }

                override fun onSlide(bottomSheet: View, slideOffset: Float) {
                    miniPlayer.alpha = 1 - slideOffset
                    playerContainer.alpha = slideOffset
                    navigationView.translationY = slideOffset * navBarHeight
                }
            })
        }
    }
}
