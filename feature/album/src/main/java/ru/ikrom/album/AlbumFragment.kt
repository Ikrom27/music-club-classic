package ru.ikrom.album

import android.os.Bundle
import android.view.View
import androidx.activity.addCallback
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import ru.ikrom.base_adapter.ThumbnailItem
import ru.ikrom.adapter_delegates.delegates.ThumbnailHeaderDelegate
import ru.ikrom.adapter_delegates.delegates.ThumbnailSmallDelegate
import ru.ikrom.base_adapter.CompositeAdapter
import ru.ikrom.base_fragment.DefaultListFragment
import ru.ikrom.theme.AppDimens
import ru.ikrom.topbar.TopBar
import ru.ikrom.ui.base_adapter.item_decorations.MarginItemDecoration
import ru.ikrom.utils.bundleToId
import javax.inject.Inject

@AndroidEntryPoint
class AlbumFragment : DefaultListFragment<AlbumPageContent, AlbumViewModel>(R.layout.fragment_album) {
    @Inject
    lateinit var navigator: Navigator
    override val mViewModel: AlbumViewModel by viewModels()
    private val compositeAdapter = CompositeAdapter.Builder()
        .add(ThumbnailHeaderDelegate(
            onPlayClick = { mViewModel.playAllTracks() },
            onShuffleClick = { mViewModel.playShuffled() },
            onArtistClick = {
                mViewModel.getArtistId()?.let {
                    navigator.toArtist(it)
                }
            }
        ))
        .add(ThumbnailSmallDelegate(
            onClick = { mViewModel.playTrack(it) },
            onLongClick = {
                navigator.toTrackMenu(it)
            })
        )
        .build()

    override fun getAdapter() = compositeAdapter
    override fun getRecyclerViewId() = R.id.rv_content
    override fun getLayoutManager() = LinearLayoutManager(requireContext())

    override fun handleState(state: AlbumPageContent) {
        compositeAdapter.setItems(listOf(state.header) + state.tracks)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewModel.updateAlbum(bundleToId(requireArguments()))
        setupRecyclerView(view)
        setupButtons(view)
        view.findViewById<TopBar>(R.id.album_bar).apply {
            setOnBackClick { navigator.toUp() }
            setOnMoreClick { }
        }
    }

    private fun setupButtons(view: View){
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner){
            navigator.toUp()
        }
        view.findViewById<TopBar>(R.id.album_bar).apply {
            setOnBackClick { navigator.toUp() }
            setOnMoreClick { mViewModel.getAlbumItem()?.let { navigator.toAlbumMenu(it) } }
        }
    }

    private fun setupRecyclerView(view: View){
        view.findViewById<RecyclerView>(R.id.rv_content).apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = compositeAdapter
            if(itemDecorationCount == 0){
                addItemDecoration(
                    MarginItemDecoration(
                        endSpace = resources.getDimensionPixelSize(AppDimens.HEIGHT_BOTTOM_NAVBAR) * 2
                    )
                )
            }
        }
    }

    interface Navigator{
        fun toArtist(artistId: String)
        fun toTrackMenu(item: ThumbnailItem)
        fun toUp()
        fun toAlbumMenu(item: ThumbnailItem)
    }
}