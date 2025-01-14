package ru.ikrom.artist

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import ru.ikrom.base_fragment.DefaultListFragment
import ru.ikrom.ui.base_adapter.CompositeAdapter
import ru.ikrom.ui.base_adapter.delegates.ArtistHeaderDelegate
import ru.ikrom.ui.base_adapter.delegates.NestedItems
import ru.ikrom.ui.base_adapter.delegates.NestedItemsDelegate
import ru.ikrom.ui.base_adapter.delegates.ThumbnailItem
import ru.ikrom.ui.base_adapter.delegates.ThumbnailLargeAdapter
import ru.ikrom.ui.base_adapter.delegates.ThumbnailRoundedDelegate
import ru.ikrom.ui.base_adapter.delegates.ThumbnailSmallDelegate
import ru.ikrom.ui.base_adapter.delegates.TitleDelegate
import ru.ikrom.ui.base_adapter.delegates.TitleItem
import ru.ikrom.ui.base_adapter.item_decorations.DecorationDimens
import ru.ikrom.ui.base_adapter.item_decorations.MarginItemDecoration
import ru.ikrom.ui.utils.bundleToId
import ru.ikrom.utils.ActionUtil
import javax.inject.Inject

@AndroidEntryPoint
class ArtistFragment : DefaultListFragment<UiState, ArtistViewModel>(R.layout.fragment_artist) {
    @Inject
    lateinit var navigator: Navigator

    override val mViewModel: ArtistViewModel by viewModels()
    private val compositeAdapter = CompositeAdapter.Builder()
        .add(TitleDelegate())
        .add(ArtistHeaderDelegate(
            onFavoriteClick = {
                mViewModel.toggleFavorite()
            },
            onShareClick = {
                context?.let { ActionUtil.shareIntent(it, mViewModel.getShareLink()) }
            },
            onPlayClick = {
                mViewModel.playAll()
            },
            onShuffleClick = {
                mViewModel.playShuffled()
            }
        ))
        .add(NestedItemsDelegate())
        .add(ThumbnailSmallDelegate({}, {}))
        .build()

    override fun getAdapter(): RecyclerView.Adapter<*> = compositeAdapter
    override fun getRecyclerViewId() = R.id.rv_content
    override fun getLayoutManager() = LinearLayoutManager(requireContext())

    override fun handleState(state: UiState) {
        when(state){
            is UiState.Success -> onStateSuccess(state)
            else -> {}
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewModel.updateArtist(bundleToId(requireArguments()))
        setupBackPass()
    }

    private fun setupBackPass(){
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigateUp()
            }
        })
    }

    private fun onStateSuccess(data: UiState.Success){
        compositeAdapter.setItems(emptyList())
        data.header?.let { compositeAdapter.addToEnd(it) }
        if(data.tracks.isNotEmpty()){
            compositeAdapter.addToEnd(TitleItem("Tracks"))
            compositeAdapter.addItems(data.tracks)
        }
        if(data.albums.isNotEmpty()){
            compositeAdapter.addToEnd(TitleItem("Albums"))
            compositeAdapter.addToEnd(
                NestedItems(
                    items = data.albums,
                    adapter = CompositeAdapter.Builder()
                        .add(ThumbnailLargeAdapter(
                            onClick = { navigator.toAlbum(it.id) },
                            onLongClick = { navigator.toAlbumMenu(it) }))
                        .build()
                )
            )
        }
        if(data.singles.isNotEmpty()){
            compositeAdapter.addToEnd(TitleItem("Singles"))
            compositeAdapter.addToEnd(
                NestedItems(
                    items = data.singles,
                    adapter = CompositeAdapter.Builder()
                        .add(ThumbnailLargeAdapter(
                            onClick = { navigator.toAlbum(it.id) },
                            onLongClick = { navigator.toAlbumMenu(it) }
                        )).build()
                )
            )
        }
        if (data.similar.isNotEmpty()) {
            compositeAdapter.addToEnd(TitleItem("Similar"))
            compositeAdapter.addToEnd(
                NestedItems(
                    items = data.similar,
                    adapter = CompositeAdapter.Builder().add(ThumbnailRoundedDelegate{
                        navigator.toArtist(it.id)
                    }).build()
                )
            )
        }
    }

    override fun recyclerViewConfigure(rv: RecyclerView) {
        super.recyclerViewConfigure(rv)
        if (rv.itemDecorationCount == 0){
            rv.addItemDecoration(
                MarginItemDecoration(
                    endSpace = DecorationDimens.getBottomMargin(resources)
                )
            )
        }
    }

    interface Navigator {
        fun toArtist(artistId: String)
        fun toAlbum(albumId: String)
        fun toAlbumMenu(info: ThumbnailItem)
    }
}