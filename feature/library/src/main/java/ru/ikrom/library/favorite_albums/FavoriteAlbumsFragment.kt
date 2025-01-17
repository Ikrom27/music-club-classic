package ru.ikrom.library.favorite_albums

import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import ru.ikrom.adapter_delegates.delegates.ThumbnailMediumPlaylistAdapter
import ru.ikrom.adapter_delegates.delegates.ThumbnailMediumPlaylistItem
import ru.ikrom.fragment_list_editable.EditableListFragment
import ru.ikrom.theme.AppStringsId
import ru.ikrom.base_adapter.CompositeAdapter
import ru.ikrom.base_adapter.ThumbnailItem
import ru.ikrom.adapter_delegates.delegates.ThumbnailRoundedSmallDelegate
import ru.ikrom.adapter_delegates.delegates.TitleDelegate
import ru.ikrom.adapter_delegates.delegates.TitleItem
import javax.inject.Inject

@AndroidEntryPoint
class FavoriteAlbumsFragment : EditableListFragment<ThumbnailMediumPlaylistItem, FavoriteAlbumsViewModel>() {
    override val mViewModel: FavoriteAlbumsViewModel by viewModels()
    @Inject
    lateinit var navigator: Navigator
    private val mAdapter = CompositeAdapter.Builder()
        .add(TitleDelegate())
        .add(ThumbnailMediumPlaylistAdapter(
            onClick = { navigator.toAlbum(it.id) },
            onLongClick = { navigator.toAlbumMenu(it) }
        ))
        .build()

    override fun getAdapter() = mAdapter

    override fun onStateSuccess(data: List<ThumbnailMediumPlaylistItem>) {
        mAdapter.setItems(listOf(
            TitleItem(getString(AppStringsId.MENU_LIKED_ARTISTS))
        ))
        mAdapter.addAll(data)
    }

    interface Navigator {
        fun toAlbum(id: String)
        fun toAlbumMenu(item: ThumbnailItem)
    }
}