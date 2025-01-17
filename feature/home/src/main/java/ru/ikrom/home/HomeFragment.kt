package ru.ikrom.home

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import ru.ikrom.base_fragment.DefaultListFragment
import ru.ikrom.base_adapter.CompositeAdapter
import ru.ikrom.adapter_delegates.delegates.CardAdapter
import ru.ikrom.adapter_delegates.delegates.CardItem
import ru.ikrom.adapter_delegates.delegates.NestedGridDelegate
import ru.ikrom.adapter_delegates.delegates.NestedGridItems
import ru.ikrom.adapter_delegates.delegates.NestedItems
import ru.ikrom.adapter_delegates.delegates.NestedItemsDelegate
import ru.ikrom.adapter_delegates.delegates.ThumbnailGridDelegate
import ru.ikrom.base_adapter.ThumbnailItem
import ru.ikrom.adapter_delegates.delegates.ThumbnailItemGrid
import ru.ikrom.adapter_delegates.delegates.ThumbnailItemMediumItem
import ru.ikrom.adapter_delegates.delegates.ThumbnailMediumAdapter
import ru.ikrom.adapter_delegates.delegates.TitleDelegate
import ru.ikrom.adapter_delegates.delegates.TitleItem
import ru.ikrom.appbar.AppBar
import ru.ikrom.theme.appBottomMargin
import ru.ikrom.ui.base_adapter.item_decorations.MarginItemDecoration
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : DefaultListFragment<UiState, HomeViewModel>(R.layout.fragment_home) {
    @Inject
    lateinit var navigator: Navigator
    override val mViewModel: HomeViewModel by viewModels()
    private lateinit var compositeAdapter: CompositeAdapter
    private lateinit var loadingProgressBar: ProgressBar

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
        loadingProgressBar.visibility = View.GONE
        when(state) {
            is UiState.Success -> showContent(state)
            is UiState.Loading -> showLoading()
        }
    }
    override fun setupItemDecorationsList(rv: RecyclerView) = listOf(
        MarginItemDecoration(
            endSpace = resources.appBottomMargin(),
        )
    )

    private fun showContent(data: UiState.Success){
        compositeAdapter.setItems(emptyList())
        if(data.quickPickTracks.isNotEmpty()){
            compositeAdapter.add(TitleItem(resources.getString(R.string.title_recommendations)))
            compositeAdapter.add(createGridNestedItem(data.quickPickTracks))
        }
        if(data.favoriteTracks.isNotEmpty()){
            compositeAdapter.add(TitleItem(resources.getString(R.string.title_favorites)))
            compositeAdapter.add(createMediumNestedItem(data.favoriteTracks))
        }
        if(data.playlists.isNotEmpty()){
            compositeAdapter.add(TitleItem(resources.getString(R.string.title_playlists)))
            compositeAdapter.add(createCardNestedItem(data.playlists))
        }
    }

    private fun showLoading(){
        loadingProgressBar.visibility = View.VISIBLE
    }

    private fun createGridNestedItem(items: List<ThumbnailItemGrid>) = NestedGridItems(
        adapter = CompositeAdapter.Builder()
            .add(ThumbnailGridDelegate(
                    onClick = { mViewModel.playTrack(it) },
                    onLongClick = { navigator.toTrackMenu(it) })
            ).build(),
        items = items
    )

    private fun createMediumNestedItem(items: List<ThumbnailItemMediumItem>) = NestedItems(
        adapter = CompositeAdapter.Builder()
            .add(ThumbnailMediumAdapter(
                    onClick = { mViewModel.playTrack(it) },
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadingProgressBar = view.findViewById(R.id.progressBar)
        view.findViewById<AppBar>(R.id.appbar).apply {
            setOnSearchClick {
                navigator.toSearchScreen()
            }
        }
    }

    interface Navigator {
        fun toTrackMenu(item: ThumbnailItem)
        fun toSearchScreen()
        fun toPlaylistMenu(item: ThumbnailItem)
        fun toPlaylist(id: String)
    }
}