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
import ru.ikrom.ui.base_adapter.delegates.ThumbnailHeaderDelegate
import ru.ikrom.ui.base_adapter.delegates.ThumbnailSmallDelegate
import dagger.hilt.android.AndroidEntryPoint
import ru.ikrom.topbar.TopBar
import ru.ikrom.ui.base_adapter.CompositeAdapter
import ru.ikrom.ui.base_adapter.delegates.ThumbnailHeaderItem
import ru.ikrom.ui.base_adapter.delegates.ThumbnailSmallItem
import ru.ikrom.ui.base_adapter.item_decorations.DecorationDimens
import ru.ikrom.ui.base_adapter.item_decorations.MarginItemDecoration

@AndroidEntryPoint
class AlbumFragment : Fragment(R.layout.fragment_album) {
    private val viewModel: AlbumViewModel by viewModels()

    private lateinit var recyclerView: RecyclerView
    private lateinit var albumBar: TopBar
    private lateinit var navController: NavController

    private val compositeAdapter = CompositeAdapter.Builder()
        .add(ThumbnailHeaderDelegate(
            onPlayClick = {
                viewModel.playAllTracks()
            },
            onShuffleClick = {
                viewModel.playShuffled()
            }
        ))
        .add(
            ThumbnailSmallDelegate(
            onClickItem = {
                viewModel.playTrackById(it.id)
            },
            onLongClickItem = {})
        )
        .build()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = requireParentFragment().findNavController()
        arguments?.getString("id")?.let { viewModel.updateAlbum(it) }
        bindViews()
        setupRecyclerView()
        setupContent()
        setupButtons()
    }

    private fun setupButtons(){
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner){
            navController.navigateUp()
        }
        albumBar.setOnBackClick {
            navController.navigateUp()
        }
        albumBar.setOnSearchClick { }
        albumBar.setOnMoreClick { }
    }

    private fun bindViews(){
        recyclerView = requireView().findViewById(R.id.rv_content)
        albumBar = requireView().findViewById(R.id.album_bar)
    }

    private fun setupRecyclerView(){
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = compositeAdapter
        if (recyclerView.itemDecorationCount == 0){
            recyclerView.addItemDecoration(
                MarginItemDecoration(
                    endSpace = DecorationDimens.getBottomMargin(resources)
                )
            )
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
}