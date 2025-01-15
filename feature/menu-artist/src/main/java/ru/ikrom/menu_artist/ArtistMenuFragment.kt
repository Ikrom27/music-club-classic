package ru.ikrom.menu_artist

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
import ru.ikrom.ui.base_adapter.delegates.ThumbnailArgs
import ru.ikrom.utils.ActionUtil
import javax.inject.Inject

@AndroidEntryPoint
class ArtistMenuFragment : BottomSheetDialogFragment(R.layout.fragment_bottom_sheet) {
    @Inject
    lateinit var navigator: Navigator
    private val viewModel: ArtistMenuViewModel by viewModels()
    private val args: ThumbnailArgs by lazy { ThumbnailArgs(requireArguments()) }
    private var compositeAdapter = CompositeAdapter.Builder()
        .add(MenuHeaderDelegate(
            onClick = { navigator.toArtist(viewModel.getArtistId()) },
            onFavoriteClick = {
                viewModel.toggleFavorite()
                dismiss()
            }
        ))
        .add(MenuButtonDelegate {
            it.onClick()
            dismiss()
        })
        .build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.setArtist(args.id, args.title, args.subtitle, args.thumbnail)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
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
        viewModel.uiContent.observe(viewLifecycleOwner) {
            compositeAdapter.setItems(getButtons())
            compositeAdapter.addFirst(it)
        }
    }

    private fun getButtons(): List<MenuButtonItem>{
        return listOf(
            MenuButtonItem(
                title = getString(AppStringsId.OPEN_ARTIST),
                icon = AppDrawableIds.ARTIST,
                onClick = {
                    navigator.toArtist(viewModel.getArtistId())
                }
            ),
            MenuButtonItem(
                title = getString(AppStringsId.SHARE),
                icon = AppDrawableIds.SHARE,
                onClick = {
                    context?.let { ActionUtil.shareIntent(it, viewModel.shareLink) }
                }
            )
        )
    }

    interface Navigator{
        fun toArtist(artistId: String)
    }

    companion object {
        private const val TAG = "BottomArtistFragment"
    }
}