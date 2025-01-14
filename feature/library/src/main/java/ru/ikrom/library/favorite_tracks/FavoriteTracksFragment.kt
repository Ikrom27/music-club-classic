package ru.ikrom.library.favorite_tracks

import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import ru.ikrom.fragment_list_editable.EditableListFragment
import ru.ikrom.theme.AppStringsId
import ru.ikrom.ui.base_adapter.CompositeAdapter
import ru.ikrom.ui.base_adapter.delegates.ThumbnailItem
import ru.ikrom.ui.base_adapter.delegates.ThumbnailSmallDelegate
import ru.ikrom.ui.base_adapter.delegates.ThumbnailSmallItem
import ru.ikrom.ui.base_adapter.delegates.TitleDelegate
import ru.ikrom.ui.base_adapter.delegates.TitleItem
import javax.inject.Inject

@AndroidEntryPoint
class FavoriteTracksFragment : EditableListFragment<ThumbnailSmallItem, FavoriteTracksViewModel>() {
    override val mViewModel: FavoriteTracksViewModel by viewModels()
    @Inject
    lateinit var navigator: Navigator
    private val mAdapter = CompositeAdapter.Builder()
        .add(TitleDelegate())
        .add(ThumbnailSmallDelegate(
            onClick = { mViewModel.playTrackById(it.id) },
            onLongClick = { navigator.toTrackMenu(it) }
        ))
        .build()

    override fun getAdapter() = mAdapter

    override fun onStateSuccess(data: List<ThumbnailSmallItem>) {
        mAdapter.setItems(listOf(
            TitleItem(getString(AppStringsId.TITLE_FAVORITE))
        ))
        mAdapter.addItems(data)
    }

    interface Navigator {
        fun toTrackMenu(item: ThumbnailItem)
    }
}