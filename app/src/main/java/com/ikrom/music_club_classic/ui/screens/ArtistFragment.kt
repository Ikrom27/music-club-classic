package com.ikrom.music_club_classic.ui.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ikrom.music_club_classic.R
import com.ikrom.music_club_classic.playback.PlayerHandler
import com.ikrom.music_club_classic.ui.adapters.delegates.ArtistHeaderDelegate
import com.ikrom.music_club_classic.ui.adapters.delegates.NestedItems
import com.ikrom.music_club_classic.ui.adapters.delegates.NestedItemsDelegate
import com.ikrom.music_club_classic.ui.adapters.delegates.ThumbnailLargeAdapter
import com.ikrom.music_club_classic.ui.adapters.delegates.ThumbnailMediumAdapter
import com.ikrom.music_club_classic.ui.adapters.delegates.ThumbnailRoundedAdapter
import com.ikrom.music_club_classic.ui.adapters.delegates.ThumbnailSmallDelegate
import com.ikrom.music_club_classic.ui.adapters.delegates.TitleDelegate
import com.ikrom.music_club_classic.ui.adapters.delegates.TitleItem
import com.ikrom.music_club_classic.viewmodel.ArtistViewModel
import dagger.hilt.android.AndroidEntryPoint
import ru.ikrom.ui.base_adapter.CompositeAdapter
import ru.ikrom.ui.base_adapter.item_decorations.MarginItemDecoration
import ru.ikrom.youtube_data.model.ArtistModel
import javax.inject.Inject


@AndroidEntryPoint
class ArtistFragment : Fragment() {
    @Inject
    lateinit var playerHandler: PlayerHandler
    private val viewModel: ArtistViewModel by viewModels()

    private lateinit var recyclerView: RecyclerView
    private lateinit var navController: NavController

    private val compositeAdapter = CompositeAdapter.Builder()
        .add(TitleDelegate())
        .add(ArtistHeaderDelegate(
            onPlayClick = {},
            onShuffleClick = {}
        ))
        .add(NestedItemsDelegate())
        .add(ThumbnailSmallDelegate({}, {}))
        .build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bundle = arguments
        navController = requireParentFragment().findNavController()
        viewModel.artistId = bundle!!.getString("id")!!
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_artist, container, false)
        bindView(view)
        setupRecyclerView()
        setupAdapterData()
        setupBackPass()
        return view
    }

    private fun setupBackPass(){
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                navController.navigateUp()
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
                    compositeAdapter.addToEnd(NestedItems(
                        items = artistPage.albums,
                        adapter = CompositeAdapter.Builder().add(ThumbnailLargeAdapter({}, {})).build()
                    ))
                }

                if (artistPage.singles.isNotEmpty()) {
                    compositeAdapter.addToEnd(TitleItem("Singles"))
                    compositeAdapter.addToEnd(NestedItems(
                        items = artistPage.singles,
                        adapter = CompositeAdapter.Builder().add(ThumbnailLargeAdapter({}, {})).build()
                    ))
                }

                if (artistPage.relatedPlaylists.isNotEmpty()) {
                    compositeAdapter.addToEnd(TitleItem("Playlist"))
                    compositeAdapter.addToEnd(NestedItems(
                        items = artistPage.relatedPlaylists,
                        adapter = CompositeAdapter.Builder().add(ThumbnailMediumAdapter({}, {})).build()
                    ))
                }

                if (artistPage.similar.isNotEmpty()) {
                    compositeAdapter.addToEnd(TitleItem("Similar"))
                    compositeAdapter.addToEnd(NestedItems(
                        items = artistPage.similar,
                        adapter = CompositeAdapter.Builder().add(ThumbnailRoundedAdapter{ similar ->
                            val artist = viewModel.artistModelLiveData.value?.similar?.find{similar.id == it.id}
                            artist?.let { onArtistClick(it) }
                        }).build()
                    ))
                }

            }

        }
    }

    private fun onArtistClick(artist: ArtistModel){
        val bundle = Bundle()
        bundle.putString("id", artist.id)
        navController.navigate(R.id.artist_to_artist, bundle)
    }

    private fun bindView(view: View) {
        recyclerView = view.findViewById(R.id.rv_content)
    }

    private fun setupRecyclerView(){
        recyclerView.adapter = compositeAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val playerHeight = resources.getDimensionPixelSize(R.dimen.mini_player_height)
        val navbarHeight = resources.getDimensionPixelSize(R.dimen.bottom_nav_bar_height)
        if (recyclerView.itemDecorationCount == 0){
            recyclerView.addItemDecoration(
                MarginItemDecoration(
                    endSpace = playerHeight + navbarHeight
                )
            )
        }
    }
}