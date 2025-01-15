package ru.ikrom.library.favorite_artists

import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import ru.ikrom.fragment_list_editable.EditableListFragment
import ru.ikrom.theme.AppStringsId
import ru.ikrom.ui.base_adapter.CompositeAdapter
import ru.ikrom.ui.base_adapter.delegates.ThumbnailItem
import ru.ikrom.ui.base_adapter.delegates.ThumbnailRoundedSmallDelegate
import ru.ikrom.ui.base_adapter.delegates.ThumbnailRoundedSmallItem
import ru.ikrom.ui.base_adapter.delegates.TitleDelegate
import ru.ikrom.ui.base_adapter.delegates.TitleItem
import javax.inject.Inject

@AndroidEntryPoint
class FavoriteArtistFragment : EditableListFragment<ThumbnailRoundedSmallItem, FavoriteArtistsViewModel>() {
    override val mViewModel: FavoriteArtistsViewModel by viewModels()
    @Inject
    lateinit var navigator: Navigator
    private val mAdapter = CompositeAdapter.Builder()
        .add(TitleDelegate())
        .add(ThumbnailRoundedSmallDelegate(
            onClick = { navigator.toArist(it.id) },
            onLongClick = { navigator.toArtistMenu(it) }
        ))
        .build()

    override fun getAdapter() = mAdapter

    override fun onStateSuccess(data: List<ThumbnailRoundedSmallItem>) {
        mAdapter.setItems(listOf(
            TitleItem(getString(AppStringsId.MENU_LIKED_ARTISTS))
        ))
        mAdapter.addAll(data)
    }

    interface Navigator {
        fun toArist(id: String)
        fun toArtistMenu(item: ThumbnailItem)
    }
}