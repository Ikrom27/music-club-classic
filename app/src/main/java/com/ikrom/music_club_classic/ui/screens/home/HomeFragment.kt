package com.ikrom.music_club_classic.ui.screens.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ikrom.music_club_classic.R
import com.ikrom.music_club_classic.ui.adapters.CompositeAdapter


class HomeFragment : Fragment() {
    val compositeAdapter = CompositeAdapter.Builder()
        .add(TrackAdapter())
        .add(PlayerAdapter())
        .build()
    val testData = listOf(
        PlayerDelegateItem(title = "Last play", content = "Numb"),
        HorizontalTracksDelegateItem(title = "favorite", tracks = listOf(1, 2, 3))
        )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.rv_main)
        compositeAdapter.swapData(testData)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = compositeAdapter
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}