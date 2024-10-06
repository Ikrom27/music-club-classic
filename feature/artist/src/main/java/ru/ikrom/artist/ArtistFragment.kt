package ru.ikrom.artist

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import ru.ikrom.ui.base_adapter.CompositeAdapter
import ru.ikrom.ui.base_adapter.delegates.ArtistHeaderDelegate
import ru.ikrom.ui.base_adapter.delegates.NestedItems
import ru.ikrom.ui.base_adapter.delegates.NestedItemsDelegate
import ru.ikrom.ui.base_adapter.delegates.ThumbnailItem
import ru.ikrom.ui.base_adapter.delegates.ThumbnailLargeAdapter
import ru.ikrom.ui.base_adapter.delegates.ThumbnailMediumAdapter
import ru.ikrom.ui.base_adapter.delegates.ThumbnailRoundedAdapter
import ru.ikrom.ui.base_adapter.delegates.ThumbnailSmallDelegate
import ru.ikrom.ui.base_adapter.delegates.TitleDelegate
import ru.ikrom.ui.base_adapter.delegates.TitleItem
import ru.ikrom.ui.base_adapter.item_decorations.DecorationDimens
import ru.ikrom.ui.base_adapter.item_decorations.MarginItemDecoration
import ru.ikrom.ui.utils.bundleToId
import javax.inject.Inject

@AndroidEntryPoint
class ArtistFragment : Fragment(R.layout.fragment_artist) {
    @Inject
    lateinit var navigator: Navigator

    private val viewModel: ArtistViewModel by viewModels()

    private val compositeAdapter = CompositeAdapter.Builder()
        .add(TitleDelegate())
        .add(ArtistHeaderDelegate(
            onPlayClick = {},
            onShuffleClick = {}
        ))
        .add(NestedItemsDelegate())
        .add(ThumbnailSmallDelegate({}, {}))
        .build()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.updateArtist(bundleToId(requireArguments()))
        setupRecyclerView(view)
        setupAdapterData()
        setupBackPass()
    }

    private fun setupBackPass(){
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigateUp()
            }
        })
    }

    private fun setupAdapterData(){
        viewModel.artistItemLiveData.observe(viewLifecycleOwner) { artistPage ->
            if (artistPage != null){
                compositeAdapter.setItems(emptyList())
                artistPage.header?.let { compositeAdapter.addToEnd(it) }
                if (artistPage.tracks.isNotEmpty()) {
                    compositeAdapter.addToEnd(TitleItem("Tracks"))
                    compositeAdapter.addItems(artistPage.tracks)
                }

                if (artistPage.albums.isNotEmpty()) {
                    compositeAdapter.addToEnd(TitleItem("Albums"))
                    compositeAdapter.addToEnd(
                        NestedItems(
                        items = artistPage.albums,
                        adapter = CompositeAdapter.Builder()
                            .add(ThumbnailLargeAdapter(
                                onClick = { navigator.toAlbum(it.id) },
                                onLongClick = { navigator.toAlbumMenu(it) }))
                            .build()
                    )
                    )
                }

                if (artistPage.singles.isNotEmpty()) {
                    compositeAdapter.addToEnd(TitleItem("Singles"))
                    compositeAdapter.addToEnd(
                        NestedItems(
                        items = artistPage.singles,
                        adapter = CompositeAdapter.Builder()
                            .add(ThumbnailLargeAdapter(
                                onClick = { navigator.toAlbum(it.id) },
                                onLongClick = { navigator.toAlbumMenu(it) }
                            )).build()
                    )
                    )
                }

                if (artistPage.relatedPlaylists.isNotEmpty()) {
                    compositeAdapter.addToEnd(TitleItem("Playlist"))
                    compositeAdapter.addToEnd(
                        NestedItems(
                        items = artistPage.relatedPlaylists,
                        adapter = CompositeAdapter.Builder()
                            .add(ThumbnailMediumAdapter(
                                onClick = {navigator.toAlbum(it.id)},
                                onLongClick = {navigator.toAlbumMenu(it)}
                            )).build()
                    )
                    )
                }

                if (artistPage.similar.isNotEmpty()) {
                    compositeAdapter.addToEnd(TitleItem("Similar"))
                    compositeAdapter.addToEnd(
                        NestedItems(
                        items = artistPage.similar,
                        adapter = CompositeAdapter.Builder().add(ThumbnailRoundedAdapter{
                            navigator.toArtist(it.id)
                        }).build()
                    )
                    )
                }

            }

        }
    }

    private fun setupRecyclerView(view: View){
        view.findViewById<RecyclerView>(R.id.rv_content).apply {
            adapter = compositeAdapter
            layoutManager = LinearLayoutManager(requireContext())
            if (itemDecorationCount == 0){
                addItemDecoration(
                    MarginItemDecoration(
                        endSpace = DecorationDimens.getBottomMargin(resources)
                    )
                )
            }
        }
    }

    interface Navigator {
        fun toArtist(artistId: String)
        fun toAlbum(albumId: String)
        fun toAlbumMenu(info: ThumbnailItem)
    }
}