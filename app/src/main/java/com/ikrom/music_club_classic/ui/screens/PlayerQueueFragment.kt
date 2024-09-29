package com.ikrom.music_club_classic.ui.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.ikrom.music_club_classic.R
import ru.ikrom.ui.base_adapter.CompositeAdapter
import com.ikrom.music_club_classic.ui.adapters.delegates.PlayerQueueDelegate
import com.ikrom.music_club_classic.ui.adapters.delegates.PlayerQueueItem
import com.ikrom.music_club_classic.ui.adapters.delegates.TitleDelegate
import com.ikrom.music_club_classic.ui.adapters.delegates.TitleItem
import com.ikrom.music_club_classic.viewmodel.PlayerQueueViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PlayerQueueFragment : BottomSheetDialogFragment() {

    val viewModel: PlayerQueueViewModel by viewModels()
    private lateinit var recyclerView: RecyclerView
    private lateinit var thumbnailImageView: ImageView
    private lateinit var titleTextView: TextView
    private lateinit var subTitleTextView: TextView

    private val compositeAdapter = CompositeAdapter.Builder()
        .add(PlayerQueueDelegate())
        .add(TitleDelegate())
        .build()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_player_queue, container, false)
        setupViews(view)
        setupRecyclerView()
        setupContent()
        setupData()
        return view
    }

    private fun setupViews(view: View) {
        recyclerView = view.findViewById(R.id.rv_content)
        thumbnailImageView = view.findViewById(R.id.iv_thumbnail_card)
        titleTextView = view.findViewById(R.id.tv_title)
        subTitleTextView = view.findViewById(R.id.tv_subtitle)
    }

    private fun setupRecyclerView(){
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = compositeAdapter
    }

    private fun setupContent(){
        viewModel.currentMediaItem?.let {
            Glide
                .with(requireContext())
                .load(it.mediaMetadata.artworkUri)
                .into(thumbnailImageView)
            titleTextView.text = it.mediaMetadata.title
            subTitleTextView.text = it.mediaMetadata.artist
        }
    }

    private fun setupData(){
        viewModel.currentQueue.observe(viewLifecycleOwner) {mediaItems ->
            compositeAdapter.setItems(emptyList())
            if (mediaItems.size > 1){
                compositeAdapter.addItems(listOf(
                    TitleItem("Current Queue")
                ))
                compositeAdapter.addItems(
                    mediaItems.map {
                        PlayerQueueItem(
                            it.mediaMetadata.title.toString(),
                            it.mediaMetadata.artist.toString(),
                            it.mediaMetadata.artworkUri.toString(),
                            it.mediaId == viewModel.currentMediaItem?.mediaId
                        )
                    }
                )
            }
            compositeAdapter.addItems(
                listOf(
                    TitleItem("Recommended")
                )
            )
            compositeAdapter.addItems(
                viewModel.recommendedQueue.map {
                    PlayerQueueItem(
                        it.mediaMetadata.title.toString(),
                        it.mediaMetadata.artist.toString(),
                        it.mediaMetadata.artworkUri.toString()
                    )
                }
            )
        }
    }
}