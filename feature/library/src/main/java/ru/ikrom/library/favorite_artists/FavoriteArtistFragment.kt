package ru.ikrom.library.favorite_artists

import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import ru.ikrom.fragment_list_editable.EditableListFragment
import ru.ikrom.theme.AppStringsId
import ru.ikrom.base_adapter.CompositeAdapter
import ru.ikrom.base_adapter.ThumbnailItem
import ru.ikrom.adapter_delegates.delegates.ThumbnailRoundedSmallDelegate
import ru.ikrom.adapter_delegates.delegates.ThumbnailRoundedSmallItem
import ru.ikrom.adapter_delegates.delegates.TitleDelegate
import ru.ikrom.adapter_delegates.delegates.TitleItem
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