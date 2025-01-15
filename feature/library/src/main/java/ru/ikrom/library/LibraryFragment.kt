package ru.ikrom.library

import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import ru.ikrom.base_fragment.DefaultListFragment
import ru.ikrom.theme.AppDrawableIds
import ru.ikrom.base_adapter.CompositeAdapter
import ru.ikrom.adapter_delegates.delegates.MenuNavigateDelegate
import ru.ikrom.adapter_delegates.delegates.MenuNavigateItem
import ru.ikrom.adapter_delegates.delegates.NestedItems
import ru.ikrom.adapter_delegates.delegates.NestedItemsDelegate
import ru.ikrom.adapter_delegates.base.ThumbnailItem
import ru.ikrom.adapter_delegates.delegates.ThumbnailMediumAdapter
import ru.ikrom.adapter_delegates.delegates.TitleDelegate
import ru.ikrom.adapter_delegates.delegates.TitleItem
import javax.inject.Inject

@AndroidEntryPoint
class LibraryFragment : DefaultListFragment<UiState, LibraryViewModel>(R.layout.fragment_library){
    @Inject
    lateinit var navigator: Navigator
    override val mViewModel: LibraryViewModel by viewModels()
    private val compositeAdapter = CompositeAdapter.Builder()
        .add(TitleDelegate())
        .add(MenuNavigateDelegate(
            onClickItem = {
                navigateMapper(it.title)
            }
        ))
        .add(NestedItemsDelegate())
        .build()

    private val screens by lazy {
        listOf(
            MenuNavigateItem(AppDrawableIds.FAVORITE, getString(R.string.menu_liked_tracks)),
            MenuNavigateItem(AppDrawableIds.ARTIST, getString(R.string.menu_liked_artists)),
            MenuNavigateItem(AppDrawableIds.AUDIO_QUALITY, getString(R.string.menu_in_device)),
        )
    }

    private fun navigateMapper(title: String) {
        when (title) {
            getString(R.string.menu_liked_tracks) -> navigator.toFavoriteTracks()
            getString(R.string.menu_liked_artists) -> navigator.toFavoriteArtists()
            getString(R.string.menu_in_device) -> navigator.toLocalTracks()
            else -> throw IllegalArgumentException("Unknown navigation title: $title")
        }
    }

    override fun getAdapter(): RecyclerView.Adapter<*> = compositeAdapter
    override fun getRecyclerViewId() = R.id.rv_main
    override fun getLayoutManager() = LinearLayoutManager(context)
    override fun handleState(state: UiState) {
        compositeAdapter.setItems(listOf(
            TitleItem(getString(R.string.title_library))
        ))
        compositeAdapter.addAll(screens)
        when(state){
            is UiState.Success -> onSuccessState(state)
            else -> {}
        }
    }

    private fun onSuccessState(data: UiState.Success){
        if(data.data.isNotEmpty()){
            compositeAdapter.add(TitleItem(getString(R.string.title_favorite)))
            compositeAdapter.add(NestedItems(
                items = data.data,
                adapter = CompositeAdapter.Builder()
                    .add(ThumbnailMediumAdapter(
                        onClick = {mViewModel.playTrack(it)},
                        onLongClick = { navigator.toTrackMenu(it) }
                )).build()
            ))
        }
    }

    interface Navigator{
        fun toTrackMenu(item: ThumbnailItem)
        fun toFavoriteTracks()
        fun toFavoriteArtists()
        fun toLocalTracks()
    }
}