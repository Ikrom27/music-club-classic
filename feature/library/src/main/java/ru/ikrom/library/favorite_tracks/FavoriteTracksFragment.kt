package ru.ikrom.library.favorite_tracks

import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import ru.ikrom.fragment_list_editable.EditableListFragment
import ru.ikrom.theme.AppStringsId
import ru.ikrom.base_adapter.CompositeAdapter
import ru.ikrom.adapter_delegates.base.ThumbnailItem
import ru.ikrom.adapter_delegates.delegates.ThumbnailSmallDelegate
import ru.ikrom.adapter_delegates.delegates.ThumbnailSmallItem
import ru.ikrom.adapter_delegates.delegates.TitleDelegate
import ru.ikrom.adapter_delegates.delegates.TitleItem
import javax.inject.Inject

@AndroidEntryPoint
class FavoriteTracksFragment : EditableListFragment<ThumbnailSmallItem, FavoriteTracksViewModel>() {
    override val mViewModel: FavoriteTracksViewModel by viewModels()
    @Inject
    lateinit var navigator: Navigator
    private val mAdapter = CompositeAdapter.Builder()
        .add(TitleDelegate())
        .add(ThumbnailSmallDelegate(
            onClick = { mViewModel.playTrack(it) },
            onLongClick = { navigator.toTrackMenu(it) }
        ))
        .build()

    override fun getAdapter() = mAdapter

    override fun onStateSuccess(data: List<ThumbnailSmallItem>) {
        mAdapter.setItems(listOf(
            TitleItem(getString(AppStringsId.TITLE_FAVORITE))
        ))
        mAdapter.addAll(data)
    }

    interface Navigator {
        fun toTrackMenu(item: ThumbnailItem)
    }
}