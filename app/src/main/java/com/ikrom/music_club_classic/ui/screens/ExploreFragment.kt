package com.ikrom.music_club_classic.ui.screens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ikrom.music_club_classic.R
import com.ikrom.music_club_classic.ui.adapters.base_adapters.CompositeAdapter
import com.ikrom.music_club_classic.ui.adapters.explore.NewReleasesDelegate
import com.ikrom.music_club_classic.ui.adapters.explore.NewReleasesDelegateItem
import com.ikrom.music_club_classic.ui.components.ActionBar
import com.ikrom.music_club_classic.viewmodel.ExploreViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ExploreFragment : Fragment() {
    private val viewModel: ExploreViewModel by viewModels()

    private lateinit var actionBar: ActionBar
    private lateinit var navController: NavController

    private lateinit var adapter: CompositeAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_explore, container, false)
        navController = requireParentFragment().findNavController()
        bindViews(view)
        setupButtons(view)
        setupAdapter()
        setupRecyclerView()
        setupAdapterData()
        return view
    }

    private fun setupAdapterData(){
        viewModel.newReleasesList.observe(viewLifecycleOwner){
            if(it.isNotEmpty()){
                if (adapter.itemCount > 1){
                    adapter.updateItem(0, NewReleasesDelegateItem("NewReleases", it))
                } else {
                    adapter.setItems(listOf(NewReleasesDelegateItem("NewReleases", it)))
                }
            }
        }
    }

    private fun setupRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
    }

    private fun setupAdapter() {
        adapter = CompositeAdapter.Builder()
            .add(NewReleasesDelegate {
                navController.navigate(R.id.action_homeFragment_to_albumFragment)
            })
            .build()
    }

    private fun setupButtons(view: View) {
        actionBar.setOnSearchClick {
            navController.navigate(R.id.action_exploreFragment_to_searchFragment)
        }
    }

    fun bindViews(view: View){
        actionBar = view.findViewById(R.id.action_bar)
        recyclerView = view.findViewById(R.id.rv_content)
    }
}