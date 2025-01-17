package ru.ikrom.artist

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import ru.ikrom.base_adapter.ThumbnailItem
import ru.ikrom.base_fragment.DefaultListFragment
import ru.ikrom.theme.AppDimens
import ru.ikrom.base_adapter.CompositeAdapter
import ru.ikrom.adapter_delegates.delegates.ArtistHeaderDelegate
import ru.ikrom.adapter_delegates.delegates.NestedItems
import ru.ikrom.adapter_delegates.delegates.NestedItemsDelegate
import ru.ikrom.adapter_delegates.delegates.ThumbnailLargeAdapter
import ru.ikrom.adapter_delegates.delegates.ThumbnailRoundedDelegate
import ru.ikrom.adapter_delegates.delegates.ThumbnailSmallDelegate
import ru.ikrom.adapter_delegates.delegates.TitleDelegate
import ru.ikrom.adapter_delegates.delegates.TitleItem
import ru.ikrom.theme.AppStringsId
import ru.ikrom.ui.base_adapter.item_decorations.MarginItemDecoration
import ru.ikrom.utils.bundleToId
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
        .add(ThumbnailSmallDelegate(
            onClick = { mViewModel.playTrack(it.id) },
            onLongClick = { navigator }))
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
        data.header?.let { compositeAdapter.add(it) }
        if(data.tracks.isNotEmpty()){
            compositeAdapter.add(TitleItem(getString(AppStringsId.TITLE_TRACKS)))
            compositeAdapter.addAll(data.tracks)
        }
        if(data.albums.isNotEmpty()){
            compositeAdapter.add(TitleItem(getString(AppStringsId.TITLE_ALBUMS)))
            compositeAdapter.add(
                NestedItems(
                    items = data.albums,
                    adapter = CompositeAdapter.Builder()
                        .add(ThumbnailLargeAdapter(
                            onClick = { navigator.toAlbum(it.id) },
                            onLongClick = { }))
                        .build()
                )
            )
        }
        if(data.singles.isNotEmpty()){
            compositeAdapter.add(TitleItem(getString(AppStringsId.TITLE_SINGLES)))
            compositeAdapter.add(
                NestedItems(
                    items = data.singles,
                    adapter = CompositeAdapter.Builder()
                        .add(ThumbnailLargeAdapter(
                            onClick = { navigator.toAlbum(it.id) },
                            onLongClick = { }
                        )).build()
                )
            )
        }
        if (data.similar.isNotEmpty()) {
            compositeAdapter.add(TitleItem(getString(AppStringsId.TITLE_SIMILAR)))
            compositeAdapter.add(
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
                    endSpace = resources.getDimensionPixelSize(AppDimens.HEIGHT_BOTTOM_NAVBAR) * 2
                )
            )
        }
    }

    interface Navigator {
        fun toArtist(artistId: String)
        fun toAlbum(albumId: String)
        fun toTrackMenu(item: ThumbnailItem)
        fun toArtistMenu(item: ThumbnailItem)
    }
}