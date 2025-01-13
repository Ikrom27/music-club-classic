package ru.ikrom.library

import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import ru.ikrom.base_fragment.DefaultListFragment
import ru.ikrom.theme.AppIconsId
import ru.ikrom.ui.base_adapter.CompositeAdapter
import ru.ikrom.ui.base_adapter.delegates.MenuNavigateDelegate
import ru.ikrom.ui.base_adapter.delegates.MenuNavigateItem
import ru.ikrom.ui.base_adapter.delegates.NestedItems
import ru.ikrom.ui.base_adapter.delegates.NestedItemsDelegate
import ru.ikrom.ui.base_adapter.delegates.ThumbnailItem
import ru.ikrom.ui.base_adapter.delegates.ThumbnailMediumAdapter
import ru.ikrom.ui.base_adapter.delegates.TitleDelegate
import ru.ikrom.ui.base_adapter.delegates.TitleItem
import javax.inject.Inject

@AndroidEntryPoint
class LibraryFragment : DefaultListFragment<UiState, LibraryViewModel>(R.layout.fragment_library){
    @Inject
    lateinit var navigator: Navigator
    override val mViewModel: LibraryViewModel by viewModels()
    private val compositeAdapter = CompositeAdapter.Builder()
        .add(TitleDelegate())
        .add(MenuNavigateDelegate(
            onClickItem = {}
        ))
        .add(NestedItemsDelegate())
        .build()

    private val screens by lazy {
        listOf(
            MenuNavigateItem(AppIconsId.favorite, getString(R.string.menu_liked_tracks)),
            MenuNavigateItem(AppIconsId.viewArtist, getString(R.string.menu_liked_artists)),
            MenuNavigateItem(AppIconsId.viewAlbum, getString(R.string.menu_liked_albums)),
            MenuNavigateItem(AppIconsId.download, getString(R.string.menu_downloaded)),
            MenuNavigateItem(AppIconsId.audioQuality, getString(R.string.menu_in_device)),
        )
    }

    override fun getAdapter(): RecyclerView.Adapter<*> = compositeAdapter
    override fun getRecyclerViewId() = R.id.rv_main
    override fun getLayoutManager() = LinearLayoutManager(context)
    override fun handleState(state: UiState) {
        compositeAdapter.setItems(listOf(
            TitleItem(getString(R.string.title_library))
        ))
        compositeAdapter.addItems(screens)
        when(state){
            is UiState.Success -> onSuccessState(state)
            else -> {}
        }
    }

    private fun onSuccessState(data: UiState.Success){
        if(data.data.isNotEmpty()){
            compositeAdapter.addToEnd(TitleItem(getString(R.string.title_favorite)))
            compositeAdapter.addToEnd(NestedItems(
                items = data.data,
                adapter = CompositeAdapter.Builder()
                    .add(ThumbnailMediumAdapter(
                        onClick = {mViewModel.playTrackById(it.id)},
                        onLongClick = { navigator.toTrackMenu(it) }
                )).build()
            ))
        }
    }

    interface Navigator{
        fun toTrackMenu(item: ThumbnailItem)
    }
}