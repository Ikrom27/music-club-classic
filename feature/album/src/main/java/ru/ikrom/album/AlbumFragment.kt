package ru.ikrom.album

import android.os.Bundle
import android.view.View
import androidx.activity.addCallback
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.ikrom.ui.base_adapter.delegates.ThumbnailHeaderDelegate
import ru.ikrom.ui.base_adapter.delegates.ThumbnailSmallDelegate
import dagger.hilt.android.AndroidEntryPoint
import ru.ikrom.topbar.TopBar
import ru.ikrom.ui.base_adapter.CompositeAdapter
import ru.ikrom.ui.base_adapter.delegates.ThumbnailHeaderItem
import ru.ikrom.ui.base_adapter.delegates.ThumbnailSmallItem
import ru.ikrom.ui.base_adapter.item_decorations.DecorationDimens
import ru.ikrom.ui.base_adapter.item_decorations.MarginItemDecoration
import javax.inject.Inject

@AndroidEntryPoint
class AlbumFragment : Fragment(R.layout.fragment_album) {
    @Inject
    lateinit var navigator: Navigator

    private val viewModel: AlbumViewModel by viewModels()

    private val args: Args by lazy { Args(requireArguments()) }
    private lateinit var navController: NavController

    private val compositeAdapter = CompositeAdapter.Builder()
        .add(ThumbnailHeaderDelegate(
            onPlayClick = { viewModel.playAllTracks() },
            onShuffleClick = { viewModel.playShuffled() },
            onArtistClick = {
                viewModel.getArtistId()?.let {
                    navigator.toArtist(it)
                }
            }
        ))
        .add(ThumbnailSmallDelegate(
            onClickItem = { viewModel.playTrackById(it.id) },
            onLongClickItem = {
                navigator.toTrackMenu(it.id, it.title, it.subtitle, it.thumbnail)
            })
        )
        .build()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = requireParentFragment().findNavController()
        viewModel.updateAlbum(args.id)
        setupRecyclerView(view)
        setupButtons(view)
        setupContent()
    }

    private fun setupButtons(view: View){
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner){
            navController.navigateUp()
        }
        view.findViewById<TopBar>(R.id.album_bar).apply {
            setOnBackClick {
                navController.navigateUp()
            }
            setOnSearchClick { }
            setOnMoreClick { }
        }
    }

    private fun setupRecyclerView(view: View){
        view.findViewById<RecyclerView>(R.id.rv_content).apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = compositeAdapter
            if(itemDecorationCount == 0){
                addItemDecoration(
                    MarginItemDecoration(
                        endSpace = DecorationDimens.getBottomMargin(resources)
                    )
                )
            }
        }
    }

    private fun setupContent() {
        viewModel.uiState.observe(viewLifecycleOwner) { state ->
            when(state) {
                AlbumUiState.Error -> showError()
                AlbumUiState.Loading -> showLoading()
                is AlbumUiState.Success -> showContent(state.header, state.tracks)
            }
        }
    }

    private fun showError(){

    }

    private fun showLoading(){

    }

    private fun showContent(header: ThumbnailHeaderItem, tracks: List<ThumbnailSmallItem>){
        compositeAdapter.setItems(listOf(header) + tracks)
    }

    class Args(bundle: Bundle){
        val id = bundle.getString(ID) ?: ""
    }

    interface Navigator{
        fun toArtist(artistId: String)
        fun toTrackMenu(trackId: String, title: String, subtitle: String, thumbnail: String)
    }

    companion object {
        const val ID = "ID"

        fun createBundle(id: String): Bundle{
            return bundleOf(ID to id)
        }
    }
}