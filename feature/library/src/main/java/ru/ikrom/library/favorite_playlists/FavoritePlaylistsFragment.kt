package ru.ikrom.library.favorite_playlists

import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import ru.ikrom.adapter_delegates.delegates.MenuButtonDelegate
import ru.ikrom.adapter_delegates.delegates.MenuButtonItem
import ru.ikrom.adapter_delegates.delegates.MenuNavigateDelegate
import ru.ikrom.adapter_delegates.delegates.MenuNavigateItem
import ru.ikrom.adapter_delegates.delegates.ThumbnailMediumPlaylistAdapter
import ru.ikrom.adapter_delegates.delegates.ThumbnailMediumPlaylistItem
import ru.ikrom.adapter_delegates.delegates.TitleDelegate
import ru.ikrom.adapter_delegates.delegates.TitleItem
import ru.ikrom.base_adapter.CompositeAdapter
import ru.ikrom.fragment_list_editable.EditableListFragment
import ru.ikrom.theme.AppDrawableIds
import ru.ikrom.theme.AppStringsId
import javax.inject.Inject

@AndroidEntryPoint
class FavoritePlaylistsFragment : EditableListFragment<ThumbnailMediumPlaylistItem, FavoritePlaylistsViewModel>() {
    override val mViewModel: FavoritePlaylistsViewModel by viewModels()
    @Inject
    lateinit var navigator: Navigator
    private val mAdapter = CompositeAdapter.Builder()
        .add(TitleDelegate())
        .add(ThumbnailMediumPlaylistAdapter(
            onClick = { navigator.toPlaylist(it.id) },
            onLongClick = { }
        ))
        .add(MenuNavigateDelegate())
        .build()

    override fun getAdapter() = mAdapter

    override fun onStateSuccess(data: List<ThumbnailMediumPlaylistItem>) {
        mAdapter.setItems(listOf(
            TitleItem(getString(AppStringsId.MENU_LIKED_ALBUMS))
        ))
        mAdapter.addAll(data)
        mAdapter.add(MenuNavigateItem(AppDrawableIds.PLUS, "Создать плейлист", { showCreatePlaylistDialog() }, false))
    }

    fun showCreatePlaylistDialog() {
        val dialog = CreatePlaylistDialog().apply {
            setListener(object : CreatePlaylistDialog.CreatePlaylistListener {
                override fun onCreatePlaylist(name: String, imageUrl: String?) {
                    mViewModel.createPlaylist(name, imageUrl ?: "")
                }
            })
        }
        dialog.show(parentFragmentManager, "CreatePlaylistDialog")
    }

    interface Navigator {
        fun toPlaylist(id: String)
    }
}