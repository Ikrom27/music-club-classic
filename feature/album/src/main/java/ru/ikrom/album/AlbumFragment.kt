package ru.ikrom.album

import android.os.Bundle
import android.view.View
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.ikrom.adapter_delegates.delegates.ThumbnailHeaderDelegate
import ru.ikrom.adapter_delegates.delegates.ThumbnailSmallDelegate
import dagger.hilt.android.AndroidEntryPoint
import ru.ikrom.adapter_delegates.base.ThumbnailItem
import ru.ikrom.theme.AppDimens
import ru.ikrom.topbar.TopBar
import ru.ikrom.adapter_delegates.delegates.ThumbnailHeaderItem
import ru.ikrom.adapter_delegates.delegates.ThumbnailSmallItem
import ru.ikrom.base_adapter.CompositeAdapter
import ru.ikrom.ui.base_adapter.item_decorations.MarginItemDecoration
import ru.ikrom.utils.bundleToId
import javax.inject.Inject

@AndroidEntryPoint
class AlbumFragment : Fragment(R.layout.fragment_album) {
    @Inject
    lateinit var navigator: Navigator

    private val viewModel: AlbumViewModel by viewModels()
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
            onClick = { viewModel.playTrack(it) },
            onLongClick = {
                navigator.toTrackMenu(it)
            })
        )
        .build()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = requireParentFragment().findNavController()
        viewModel.updateAlbum(bundleToId(requireArguments()))
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
                        endSpace = resources.getDimensionPixelSize(AppDimens.HEIGHT_BOTTOM_NAVBAR) * 2
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

    interface Navigator{
        fun toArtist(artistId: String)
        fun toTrackMenu(item: ThumbnailItem)
    }
}