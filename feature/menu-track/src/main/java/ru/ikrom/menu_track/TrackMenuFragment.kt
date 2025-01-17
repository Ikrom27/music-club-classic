package ru.ikrom.menu_track

import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import ru.ikrom.adapter_delegates.delegates.MenuButtonDelegate
import ru.ikrom.adapter_delegates.delegates.MenuButtonItem
import ru.ikrom.adapter_delegates.delegates.MenuHeaderDelegate
import ru.ikrom.base_adapter.CompositeAdapter
import ru.ikrom.fragment_bottom_menu.BaseBottomMenuFragment
import ru.ikrom.theme.AppDrawableIds
import ru.ikrom.theme.AppStringsId
import ru.ikrom.utils.ActionUtil
import javax.inject.Inject

@AndroidEntryPoint
class TrackMenuFragment : BaseBottomMenuFragment<TrackMenuViewModel>() {
    @Inject
    lateinit var navigator: Navigator
    override val mViewModel: TrackMenuViewModel by viewModels()

    override val mAdapter = CompositeAdapter.Builder()
        .add(MenuHeaderDelegate(
            onClick = { navigator.toArtist(mViewModel.getArtistId()) },
            onFavoriteClick = { mViewModel.toggleFavorite() }
        ))
        .add(MenuButtonDelegate {
            it.onClick()
            dismiss()
        })
        .build()


    override fun getMenuButtons(): List<MenuButtonItem>{
        return listOf(
            MenuButtonItem(
                title = getString(AppStringsId.ADD_TO_QUEUE),
                icon = AppDrawableIds.ADD_TO_QUEUE,
                onClick = { mViewModel.addToQueue() }
            ),
            MenuButtonItem(
                title = getString(AppStringsId.TO_DOWNLOAD),
                icon = AppDrawableIds.DOWNLOAD,
                onClick = { mViewModel.download() }
            ),
            MenuButtonItem(
                title = getString(AppStringsId.OPEN_ALBUM),
                icon = AppDrawableIds.ALBUM,
                onClick = {
                    navigator.toAlbum(mViewModel.getAlbumId())
                }
            ),
            MenuButtonItem(
                title = getString(AppStringsId.OPEN_ARTIST),
                icon = AppDrawableIds.ARTIST,
                onClick = {
                    navigator.toArtist(mViewModel.getArtistId())
                }
            ),
            MenuButtonItem(
                title = getString(AppStringsId.SHARE),
                icon = AppDrawableIds.SHARE,
                onClick = {
                    context?.let { ActionUtil.shareIntent(it, mViewModel.shareLink) }
                }
            )
        )
    }

    interface Navigator{
        fun toAlbum(albumId: String)
        fun toArtist(artistId: String)
    }

    companion object {
        private const val TAG = "BottomMenuFragment"
    }
}