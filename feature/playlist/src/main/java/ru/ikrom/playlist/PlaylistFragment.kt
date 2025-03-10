package ru.ikrom.playlist

import android.os.Bundle
import android.view.View
import androidx.activity.addCallback
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import ru.ikrom.adapter_delegates.delegates.ThumbnailHeaderDelegate
import ru.ikrom.adapter_delegates.delegates.ThumbnailSmallDelegate
import ru.ikrom.base_adapter.CompositeAdapter
import ru.ikrom.base_adapter.ThumbnailItem
import ru.ikrom.base_fragment.DefaultListFragment
import ru.ikrom.playlist.tracks_choise.ChoiceTracksDialog
import ru.ikrom.theme.AppDimens
import ru.ikrom.topbar.TopBar
import ru.ikrom.ui.base_adapter.item_decorations.MarginItemDecoration
import ru.ikrom.utils.bundleToId
import javax.inject.Inject

@AndroidEntryPoint
class PlaylistFragment : DefaultListFragment<PlaylistPageContent, PlaylistViewModel>(R.layout.fragment_playlist) {
    @Inject
    lateinit var navigator: Navigator

    override val mViewModel: PlaylistViewModel by viewModels()
    private val mAdapter = CompositeAdapter.Builder()
        .add(ThumbnailHeaderDelegate(
            onPlayClick = { mViewModel.playAllTracks() },
            onShuffleClick = { mViewModel.playShuffled() },
            onArtistClick = { }
        ))
        .add(
            ThumbnailSmallDelegate(
            onClick = { mViewModel.playTrack(it) },
            onLongClick = {
                navigator.toTrackMenu(it)
            })
        )
        .build()

    override fun getAdapter() = mAdapter
    override fun getRecyclerViewId() = R.id.rv_content
    override fun getLayoutManager() = LinearLayoutManager(requireContext())

    override fun handleState(state: PlaylistPageContent) {
        mAdapter.setItems(listOf(state.header) + state.tracks)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewModel.setupPlaylist(bundleToId(requireArguments()))
        setupRecyclerView(view)
        setupButtons(view)
        view.findViewById<TopBar>(R.id.album_bar).apply {
            setOnBackClick { navigator.toUp() }
            setOnMoreClick { mViewModel.setupPlaylist(bundleToId(requireArguments())) }
            setOnAddClick { showChoiceTracksDialog() }
        }
    }

    private fun setupButtons(view: View){
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner){
            navigator.toUp()
        }
        view.findViewById<TopBar>(R.id.album_bar).apply {
            setOnBackClick { navigator.toUp() }
            setOnMoreClick { mViewModel.getPlaylistItems()?.let { navigator.toAlbumMenu(it) } }
        }
    }

    private fun setupRecyclerView(view: View){
        view.findViewById<RecyclerView>(R.id.rv_content).apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = mAdapter
            if(itemDecorationCount == 0){
                addItemDecoration(
                    MarginItemDecoration(
                        endSpace = resources.getDimensionPixelSize(AppDimens.HEIGHT_BOTTOM_NAVBAR) * 2
                    )
                )
            }
        }
    }

    private fun showChoiceTracksDialog() {
        val dialog = ChoiceTracksDialog(bundleToId(requireArguments()))
        println(bundleToId(requireArguments()))
        dialog.show(parentFragmentManager, dialog.tag)
    }

    interface Navigator{
        fun toUp()
        fun toAlbumMenu(item: ThumbnailItem)
        fun toTrackMenu(item: ThumbnailItem)
    }
}