package ru.ikrom.menu_track

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import ru.ikrom.theme.AppIconsId
import ru.ikrom.theme.AppStringsId
import ru.ikrom.ui.base_adapter.CompositeAdapter
import ru.ikrom.ui.base_adapter.delegates.MenuButtonDelegate
import ru.ikrom.ui.base_adapter.delegates.MenuButtonItem
import ru.ikrom.ui.base_adapter.delegates.MenuHeaderDelegate
import ru.ikrom.ui.base_adapter.delegates.MenuHeaderDelegateItem
import javax.inject.Inject

@AndroidEntryPoint
class TrackMenuFragment : BottomSheetDialogFragment(R.layout.fragment_bottom_sheet) {
    @Inject
    lateinit var navigator: Navigator
    private val viewModel: TrackMenuViewModel by viewModels()
    private val args: Args by lazy { Args(requireArguments()) }
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
                icon = AppIconsId.addToQueue,
                onClick = { viewModel.addToQueue() }
            ),
            MenuButtonItem(
                title = getString(AppStringsId.TO_DOWNLOAD),
                icon = AppIconsId.download,
                onClick = { viewModel.download() }
            ),
            MenuButtonItem(
                title = getString(AppStringsId.OPEN_ALBUM),
                icon = AppIconsId.viewAlbum,
                onClick = {
                    navigator.toAlbum(findNavController(), viewModel.getAlbumId())
                }
            ),
            MenuButtonItem(
                title = getString(AppStringsId.OPEN_ARTIST),
                icon = AppIconsId.viewArtist,
                onClick = {
                    navigator.toArtist(findNavController(), viewModel.getArtistId())
                }
            )
        )
    }

    interface Navigator{
        fun toAlbum(nanController: NavController, id: String)
        fun toArtist(nanController: NavController, id: String)
    }

    inner class Args(bundle: Bundle) {
        val id: String = bundle.getString(ID) ?: ""
        val title: String = bundle.getString(TITLE) ?: ""
        val subtitle: String = bundle.getString(SUBTITLE) ?: ""
        val thumbnail: String = bundle.getString(THUMBNAIL) ?: ""
    }

    companion object {
        private const val TAG = "BottomMenuFragment"
        private const val ID = "id"
        private const val TITLE = "title"
        private const val SUBTITLE = "subtitle"
        private const val THUMBNAIL = "thumbnail"

        fun createBundle(
            id: String,
            title: String,
            subtitle: String,
            thumbnail: String
        ): Bundle {
            return bundleOf(
                ID to id,
                TITLE to title,
                SUBTITLE to subtitle,
                THUMBNAIL to thumbnail
            )
        }
    }
}