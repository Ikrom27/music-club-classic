package ru.ikrom.home

import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import ru.ikrom.base_fragment.DefaultListFragment
import ru.ikrom.ui.base_adapter.CompositeAdapter
import ru.ikrom.ui.base_adapter.delegates.CardAdapter
import ru.ikrom.ui.base_adapter.delegates.CardItem
import ru.ikrom.ui.base_adapter.delegates.NestedGridDelegate
import ru.ikrom.ui.base_adapter.delegates.NestedGridItems
import ru.ikrom.ui.base_adapter.delegates.NestedItems
import ru.ikrom.ui.base_adapter.delegates.NestedItemsDelegate
import ru.ikrom.ui.base_adapter.delegates.ThumbnailGridDelegate
import ru.ikrom.ui.base_adapter.delegates.ThumbnailItem
import ru.ikrom.ui.base_adapter.delegates.ThumbnailItemGrid
import ru.ikrom.ui.base_adapter.delegates.ThumbnailItemMediumItem
import ru.ikrom.ui.base_adapter.delegates.ThumbnailMediumAdapter
import ru.ikrom.ui.base_adapter.delegates.TitleDelegate
import ru.ikrom.ui.base_adapter.delegates.TitleItem
import ru.ikrom.ui.base_adapter.item_decorations.DecorationDimens
import ru.ikrom.ui.base_adapter.item_decorations.MarginItemDecoration
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : DefaultListFragment<UiState, HomeViewModel>(R.layout.fragment_home) {
    @Inject
    lateinit var navigator: Navigator
    override val mViewModel: HomeViewModel by viewModels()
    private lateinit var compositeAdapter: CompositeAdapter

    override fun getAdapter(): RecyclerView.Adapter<*> {
        return CompositeAdapter.Builder()
            .add(NestedItemsDelegate())
            .add(NestedGridDelegate())
            .add(TitleDelegate())
            .build()
            .also {
                compositeAdapter = it
            }
    }
    override fun getRecyclerViewId() = R.id.rv_main
    override fun getLayoutManager() = LinearLayoutManager(context)
    override fun handleState(state: UiState) {
        mViewModel.state.observe(viewLifecycleOwner) { state ->
            when(state){
                is UiState.Success -> showContent(state)
                else -> {}
            }
        }
    }

    override fun recyclerViewConfigure(rv: RecyclerView) {
        super.recyclerViewConfigure(rv)
        if (rv.itemDecorationCount == 0){
            rv.addItemDecoration(
                MarginItemDecoration(
                    startSpace = 0,
                    endSpace = DecorationDimens.getBottomMargin(resources) * 2,
                )
            )
        }
    }

    private fun showContent(data: UiState.Success){
        compositeAdapter.setItems(emptyList())
        if(data.quickPickTracks.isNotEmpty()){
            compositeAdapter.addToEnd(TitleItem(resources.getString(R.string.title_recommendations)))
            compositeAdapter.addToEnd(createGridNestedItem(data.quickPickTracks))
        }
        if(data.favoriteTracks.isNotEmpty()){
            compositeAdapter.addToEnd(TitleItem(resources.getString(R.string.title_favorites)))
            compositeAdapter.addToEnd(createMediumNestedItem(data.favoriteTracks))
        }
        if(data.playlists.isNotEmpty()){
            compositeAdapter.addToEnd(TitleItem(resources.getString(R.string.title_playlists)))
            compositeAdapter.addToEnd(createCardNestedItem(data.playlists))
        }
    }

    private fun createGridNestedItem(items: List<ThumbnailItemGrid>) = NestedGridItems(
        adapter = CompositeAdapter.Builder()
            .add(ThumbnailGridDelegate(
                    onClick = { mViewModel.playTrackById(it.id) },
                    onLongClick = { navigator.toTrackMenu(it) })
            ).build(),
        items = items
    )

    private fun createMediumNestedItem(items: List<ThumbnailItemMediumItem>) = NestedItems(
        adapter = CompositeAdapter.Builder()
            .add(ThumbnailMediumAdapter(
                    onClick = { mViewModel.playTrackById(it.id) },
                    onLongClick = { navigator.toTrackMenu(it) }
                )).build(),
        items = items
    )

    private fun createCardNestedItem(items: List<CardItem>) = NestedItems(
        adapter = CompositeAdapter.Builder()
            .add(CardAdapter(
                    onClick = { navigator.toPlaylist(it.id) },
                    onLongClick = { navigator.toPlaylistMenu(it) }
                ) ).build(),
        items = items
    )

    interface Navigator {
        fun toTrackMenu(item: ThumbnailItem)
        fun toPlaylistMenu(item: ThumbnailItem)
        fun toPlaylist(id: String)
    }
}