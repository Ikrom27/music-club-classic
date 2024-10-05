package ru.ikrom.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import ru.ikrom.ui.base_adapter.CompositeAdapter
import ru.ikrom.ui.base_adapter.delegates.CardAdapter
import ru.ikrom.ui.base_adapter.delegates.NestedItems
import ru.ikrom.ui.base_adapter.delegates.NestedItemsDelegate
import ru.ikrom.ui.base_adapter.delegates.QuickPickDelegate
import ru.ikrom.ui.base_adapter.delegates.QuickPickItem
import ru.ikrom.ui.base_adapter.delegates.ThumbnailMediumAdapter
import ru.ikrom.ui.base_adapter.delegates.TitleDelegate
import ru.ikrom.ui.base_adapter.delegates.TitleItem
import ru.ikrom.ui.base_adapter.item_decorations.DecorationDimens
import ru.ikrom.ui.base_adapter.item_decorations.MarginItemDecoration
import ru.ikrom.youtube_data.model.TrackModel
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {
    @Inject
    lateinit var navigator: Navigator

    private lateinit var navController: NavController
    private val homeViewModel: HomeViewModel by viewModels()
    private lateinit var compositeAdapter: CompositeAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.rv_main)
        navController = requireParentFragment().findNavController()
        setupAdapter()
        setupRecyclerView(recyclerView)
        setupAdapterData()
        return view
    }

    private fun setupAdapter(){
        compositeAdapter = CompositeAdapter.Builder()
            .add(QuickPickDelegate(
                isPlaying = homeViewModel.isPlaying,
//                currentMediaItem= homeViewModel.currentTrack,
                lifecycleOwner = viewLifecycleOwner,
                onPlayPauseClick = { onPlayPauseClick(it) },
                onSkipClick = {}
            ))
            .add(NestedItemsDelegate())
            .add(TitleDelegate())
            .build()
    }

    private fun setupAdapterData(){
        compositeAdapter.setItems(listOf(
            TitleItem("Quick pick"),
            QuickPickItem(track = null),
            TitleItem(""),
            TitleItem(""),
            TitleItem(""),
            TitleItem("")
        ))
        homeViewModel.quickPick.observe(viewLifecycleOwner) { tracks ->
            if (tracks.isNotEmpty()){
                compositeAdapter.updateItem(1,
                    QuickPickItem(track = tracks[0]))
            }
        }
        homeViewModel.userPlaylists.observe(viewLifecycleOwner) {playlists ->
            if (playlists.isNotEmpty()){
                compositeAdapter.updateItem(2, TitleItem("Your playlists"))
                compositeAdapter.updateItem(3,
                    NestedItems(
                        items = playlists,
                        adapter = CompositeAdapter.Builder()
                            .add(
                                CardAdapter(
                                onClick = {},
                                onLongClick = {})
                            )
                            .build()
                    )
                )
            }
        }
        homeViewModel.trackList.observe(viewLifecycleOwner) {tracks ->
            if (tracks.isNotEmpty()){
                compositeAdapter.updateItem(4, TitleItem("From Linkin park"))
                compositeAdapter.updateItem(5,
                    NestedItems(
                        adapter = CompositeAdapter.Builder()
                            .add(
                                ThumbnailMediumAdapter(
                                onClick = {
                                    homeViewModel.playTrackById(it.id)
                                },
                                onLongClick = {
                                    findNavController().navigate(
                                        navigator.menuTrackId,
                                        navigator.bundleMenuTrack(
                                            it.id,
                                            it.title,
                                            it.subtitle,
                                            it.thumbnail
                                        )
                                    )
                                }
                            )
                        ).build(),
                        items = tracks
                    )
                )
            }
        }
    }

    private fun setupRecyclerView(recyclerView: RecyclerView){
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = compositeAdapter
        if (recyclerView.itemDecorationCount == 0){
            recyclerView.addItemDecoration(
                MarginItemDecoration(
                    endSpace = DecorationDimens.getBottomMargin(resources),
                )
            )
        }
    }

    private fun onPlayPauseClick(track: TrackModel){
        if (homeViewModel.currentTrack.value!!.mediaId == track.videoId){
            homeViewModel.togglePlayPause()
        }
    }

    interface Navigator {
        val menuTrackId: Int
        fun bundleMenuTrack(
            id: String,
            title: String,
            subtitle: String,
            thumbnail: String
        ): Bundle
    }
}