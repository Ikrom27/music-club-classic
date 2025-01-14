package ru.ikrom.menu_track

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import ru.ikrom.theme.AppDrawableIds
import ru.ikrom.theme.AppStringsId
import ru.ikrom.ui.base_adapter.CompositeAdapter
import ru.ikrom.ui.base_adapter.delegates.MenuButtonDelegate
import ru.ikrom.ui.base_adapter.delegates.MenuButtonItem
import ru.ikrom.ui.base_adapter.delegates.MenuHeaderDelegate
import ru.ikrom.ui.base_adapter.delegates.MenuHeaderDelegateItem
import ru.ikrom.ui.base_adapter.delegates.ThumbnailArgs
import ru.ikrom.utils.ActionUtil
import javax.inject.Inject

@AndroidEntryPoint
class TrackMenuFragment : BottomSheetDialogFragment(R.layout.fragment_bottom_sheet) {
    @Inject
    lateinit var navigator: Navigator
    private val viewModel: TrackMenuViewModel by viewModels()
    private val args: ThumbnailArgs by lazy { ThumbnailArgs(requireArguments()) }
    private var compositeAdapter = CompositeAdapter.Builder()
        .add(MenuHeaderDelegate({}))
        .add(MenuButtonDelegate {
            it.onClick()
            dismiss()
        })
        .build()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.setTrackById(args.id)
        setupRecyclerView(view)
        setupItems()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun setupRecyclerView(view: View) {
        view.findViewById<RecyclerView>(R.id.rv_content).apply {
            layoutManager = LinearLayoutManager(context)
            adapter = compositeAdapter
        }
    }

    private fun setupItems() {
        compositeAdapter.addToStart(
            MenuHeaderDelegateItem(
                args.title,
                args.subtitle,
                args.thumbnail
            )
        )
        compositeAdapter.addItems(getButtons())
    }

    private fun getButtons(): List<MenuButtonItem>{
        return listOf(
            MenuButtonItem(
                title = getString(AppStringsId.ADD_TO_QUEUE),
                icon = AppDrawableIds.addToQueue,
                onClick = { viewModel.addToQueue() }
            ),
            MenuButtonItem(
                title = getString(AppStringsId.TO_DOWNLOAD),
                icon = AppDrawableIds.download,
                onClick = { viewModel.download() }
            ),
            MenuButtonItem(
                title = getString(AppStringsId.OPEN_ALBUM),
                icon = AppDrawableIds.viewAlbum,
                onClick = {
                    navigator.toAlbum(viewModel.getAlbumId())
                }
            ),
            MenuButtonItem(
                title = getString(AppStringsId.OPEN_ARTIST),
                icon = AppDrawableIds.viewArtist,
                onClick = {
                    navigator.toArtist(viewModel.getArtistId())
                }
            ),
            MenuButtonItem(
                title = getString(AppStringsId.SHARE),
                icon = AppDrawableIds.share,
                onClick = {
                    context?.let { ActionUtil.shareIntent(it, viewModel.shareLink) }
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