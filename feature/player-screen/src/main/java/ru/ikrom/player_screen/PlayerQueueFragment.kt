package ru.ikrom.player_screen

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import ru.ikrom.ui.base_adapter.CompositeAdapter
import ru.ikrom.ui.base_adapter.delegates.PlayerQueueDelegate
import ru.ikrom.ui.base_adapter.delegates.TitleDelegate
import ru.ikrom.ui.base_adapter.delegates.TitleItem


@AndroidEntryPoint
class PlayerQueueFragment : BottomSheetDialogFragment(R.layout.fragment_player_queue) {

    private val viewModel: PlayerQueueViewModel by viewModels()

    private lateinit var recyclerView: RecyclerView
    private lateinit var thumbnailImageView: ImageView
    private lateinit var titleTextView: TextView
    private lateinit var subTitleTextView: TextView

    private val compositeAdapter by lazy {
        CompositeAdapter.Builder()
            .add(PlayerQueueDelegate().apply {
                viewModel.currentMediaItem.observe(viewLifecycleOwner)@SuppressLint("NotifyDataSetChanged") {
                    currentTrackId = it?.mediaId ?: ""
                    recyclerView.adapter?.notifyDataSetChanged()
                }
            })
            .add(TitleDelegate())
            .build()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews(view)
        setupRecyclerView()
        setupContent()
        setupData()
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
        viewModel.currentMediaItem.value?.let {
            Glide
                .with(requireContext())
                .load(it.mediaMetadata.artworkUri)
                .into(thumbnailImageView)
            titleTextView.text = it.mediaMetadata.title
            subTitleTextView.text = it.mediaMetadata.artist
        }
    }

    private fun setupData(){
        viewModel.currentQueue.observe(viewLifecycleOwner) {tracks ->
            compositeAdapter.setItems(listOf(TitleItem("Current Queue")))
            compositeAdapter.addItems(tracks)
        }
    }
}