package com.ikrom.music_club_classic.ui.components

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.ikrom.music_club_classic.R
import com.ikrom.music_club_classic.data.model.Track
import com.ikrom.music_club_classic.extensions.toDp
import com.ikrom.music_club_classic.extensions.toMediaItem
import com.ikrom.music_club_classic.playback.PlayerHandler
import com.ikrom.music_club_classic.ui.adapters.account.ButtonsAdapter
import com.ikrom.music_club_classic.ui.adapters.account.ButtonsDelegateItem
import com.ikrom.music_club_classic.ui.adapters.base_adapters.CompositeAdapter
import com.ikrom.music_club_classic.ui.adapters.base_adapters.IDelegateItem
import com.ikrom.music_club_classic.ui.adapters.bottom_menu.MenuButtonDelegate
import com.ikrom.music_club_classic.ui.adapters.bottom_menu.MenuButtonItem
import com.ikrom.music_club_classic.ui.adapters.bottom_menu.MenuHeaderDelegate
import com.ikrom.music_club_classic.ui.adapters.bottom_menu.MenuHeaderDelegateItem
import com.ikrom.music_club_classic.viewmodel.AlbumViewModel
import com.ikrom.music_club_classic.viewmodel.BottomMenuViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * menu items:
 * - add to queue
 * - save in library
 * - download
 * - open album
 * - open artist
 * - share
 */

@AndroidEntryPoint
class BottomMenuFragment : BottomSheetDialogFragment() {

    @Inject
    lateinit var playerHandler: PlayerHandler

    private val viewModel: BottomMenuViewModel by activityViewModels()
    private val albumViewModel: AlbumViewModel by activityViewModels()

    private lateinit var recyclerView: RecyclerView
    private lateinit var navController: NavController
    private var compositeAdapter = CompositeAdapter.Builder()
        .add(MenuHeaderDelegate(onClickListener = {}))
        .add(MenuButtonDelegate())
        .build()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_bottom_sheet, container, false)
        navController = requireParentFragment().findNavController()
        setupViews(view)
        setupRecyclerView()
        setupItems()
        return view
    }

    private fun setupViews(view: View) {
        recyclerView = view.findViewById(R.id.rv_content)
    }

    private fun setupRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = compositeAdapter
    }

    private fun setupItems() {
        Log.d(TAG, viewModel.trackLiveData.value!!.title)
        viewModel.trackLiveData.observe(viewLifecycleOwner) {track ->
            compositeAdapter.addToStart(MenuHeaderDelegateItem(track))
            compositeAdapter.addItems(getButtonsList(track))
        }
    }

    private fun getButtonsList(track: Track): List<MenuButtonItem> {
        return listOf(
            MenuButtonItem(
                title = getString(R.string.add_to_queue),
                icon = R.drawable.ic_add_to_queue,
                onClick = {playerHandler.addToQueue(track.toMediaItem())}
            ),
            MenuButtonItem(
                title = getString(R.string.to_download),
                icon = R.drawable.ic_download,
                onClick = {}
            ),
            MenuButtonItem(
                title = getString(R.string.open_album),
                icon = R.drawable.ic_view_album,
                onClick = {
                    albumViewModel.setAlbum(track.album)
                    navController.navigate(R.id.action_homeFragment_to_albumFragment2)
                }
            ),
            MenuButtonItem(
                title = getString(R.string.open_artist),
                icon = R.drawable.ic_view_artist,
                onClick = {}
            ),
            MenuButtonItem(
                title = getString(R.string.share),
                icon = R.drawable.ic_share,
                onClick = {}
            )
        )
    }


    companion object {
        const val TAG = "BottomMenuFragment"
    }
}