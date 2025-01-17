package ru.ikrom.menu_album

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
class AlbumMenuFragment : BaseBottomMenuFragment<AlbumMenuViewModel>() {
    @Inject
    lateinit var navigator: Navigator
    override val mViewModel: AlbumMenuViewModel by viewModels()
    override val mAdapter = CompositeAdapter.Builder()
        .add(MenuHeaderDelegate(
            onClick = {  navigator.toAlbum(mViewModel.getAlbumId()) },
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
                title =  getString(AppStringsId.OPEN_ALBUM),
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
        fun toAlbum(id: String)
        fun toArtist(id: String)
    }

    companion object {
        private const val TAG = "BottomArtistFragment"
    }
}