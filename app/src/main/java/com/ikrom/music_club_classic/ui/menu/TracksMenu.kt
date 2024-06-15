package com.ikrom.music_club_classic.ui.menu

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.ikrom.music_club_classic.R
import com.ikrom.music_club_classic.data.model.Track
import com.ikrom.music_club_classic.extensions.toMediaItem
import com.ikrom.music_club_classic.playback.PlayerHandler
import com.ikrom.base_adapter.CompositeAdapter
import com.ikrom.music_club_classic.extensions.models.toMenuHeaderItem
import com.ikrom.music_club_classic.ui.adapters.delegates.MenuButtonDelegate
import com.ikrom.music_club_classic.ui.adapters.delegates.MenuButtonItem
import com.ikrom.music_club_classic.ui.adapters.delegates.MenuHeaderDelegate
import com.ikrom.music_club_classic.viewmodel.AlbumViewModel
import com.ikrom.music_club_classic.viewmodel.BottomMenuViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class TracksMenu : BottomSheetDialogFragment() {

    @Inject
    lateinit var playerHandler: PlayerHandler

    private val viewModel: BottomMenuViewModel by activityViewModels()
    private val albumViewModel: AlbumViewModel by activityViewModels()

    private lateinit var recyclerView: RecyclerView
    private lateinit var navController: NavController
    private var compositeAdapter = CompositeAdapter.Builder()
        .add(MenuHeaderDelegate())
        .add(MenuButtonDelegate())
        .build()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_bottom_sheet, container, false)
        navController = viewModel.navController!!
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
            compositeAdapter.addToStart(track.toMenuHeaderItem {})
            compositeAdapter.addItems(getButtonsList(track))
        }
    }

    private fun getButtonsList(track: Track): List<MenuButtonItem> {
        return listOf(
            MenuButtonItem(
                title = getString(R.string.add_to_queue),
                icon = R.drawable.ic_add_to_queue,
                onClick = {
                    playerHandler.addToQueue(track.toMediaItem())
                    dismiss()
                }
            ),
            MenuButtonItem(
                title = getString(R.string.to_download),
                icon = R.drawable.ic_download,
                onClick = {
                    dismiss()
                }
            ),
            MenuButtonItem(
                title = getString(R.string.open_album),
                icon = R.drawable.ic_view_album,
                onClick = {
                    albumViewModel.setAlbum(track.album)
                    navController.navigate(R.id.home_to_album)
                    dismiss()
                }
            ),
            MenuButtonItem(
                title = getString(R.string.open_artist),
                icon = R.drawable.ic_view_artist,
                onClick = {
//                    artistViewModel.artistId = track.album.artists?.first()?.id ?: ""
                    val bundle = Bundle()
                    bundle.putString("id", track.album.artists?.first()?.id ?: "")
                    navController.navigate(R.id.home_to_artist, bundle)
                    dismiss()
                }
            ),
            MenuButtonItem(
                title = getString(R.string.share),
                icon = R.drawable.ic_share,
                onClick = {
                    dismiss()
                }
            )
        )
    }

    companion object {
        const val TAG = "BottomMenuFragment"
    }
}