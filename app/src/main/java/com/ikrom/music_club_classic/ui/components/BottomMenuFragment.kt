package com.ikrom.music_club_classic.ui.components

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.ikrom.music_club_classic.R
import com.ikrom.music_club_classic.ui.adapters.base_adapters.CompositeAdapter
import com.ikrom.music_club_classic.ui.adapters.bottom_menu.MenuHeaderDelegate
import com.ikrom.music_club_classic.ui.adapters.bottom_menu.MenuHeaderDelegateItem
import com.ikrom.music_club_classic.viewmodel.BottomMenuViewModel

/**
 * menu items:
 * - add to queue
 * - save in library
 * - download
 * - open album
 * - open artist
 * - share
 */

class BottomMenuFragment : BottomSheetDialogFragment() {
    private lateinit var recyclerView: RecyclerView

    private val viewModel: BottomMenuViewModel by activityViewModels()

    private var compositeAdapter = CompositeAdapter.Builder()
        .add(
            MenuHeaderDelegate(onClickListener = {})
        )
        .build()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_bottom_sheet, container, false)
        setupViews(view)
        setupRecyclerView()
        setupItems()
        return view
    }

    private fun setupViews(view: View) {
        recyclerView = view.findViewById(R.id.rv_content)
    }

    private fun setupRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = compositeAdapter
    }

    private fun setupItems() {
        Log.d(TAG, viewModel.trackLiveData.value!!.title)
        viewModel.trackLiveData.observe(viewLifecycleOwner) {track ->
            compositeAdapter.addToStart(MenuHeaderDelegateItem(track))
        }
    }

    companion object {
        const val TAG = "BottomMenuFragment"
    }
}