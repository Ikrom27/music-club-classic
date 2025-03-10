package ru.ikrom.playlist.tracks_choise

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import ru.ikrom.adapter_delegates.delegates.ThumbnailSmallDelegate
import ru.ikrom.base_adapter.CompositeAdapter
import ru.ikrom.playlist.R

@AndroidEntryPoint
class ChoiceTracksDialog(val id: String? = null): BottomSheetDialogFragment(R.layout.fragment_choice_tracks) {
    private val viewModel: ChoiceTracksViewModel by viewModels()

    private val mAdapter = CompositeAdapter.Builder()
        .add(ThumbnailSmallDelegate(
            onClick = {
                viewModel.addToPlaylist(it.id)
            },
            onLongClick = {}
        ))
        .build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        id ?: dismiss()
        viewModel.fetchData(id!!)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<RecyclerView>(R.id.rv_content).apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = mAdapter
        }
        viewModel.tracksToChoice.observe(viewLifecycleOwner) {
            mAdapter.setItems(it)
        }
    }
}