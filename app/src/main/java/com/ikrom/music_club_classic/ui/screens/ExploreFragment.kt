package com.ikrom.music_club_classic.ui.screens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.ikrom.music_club_classic.R
import com.ikrom.music_club_classic.ui.components.ActionBar


class ExploreFragment : Fragment() {
    private lateinit var actionBar: ActionBar
    private lateinit var navController: NavController
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_explore, container, false)
        navController = requireParentFragment().findNavController()
        bindViews(view)
        setupButtons(view)
        return view
    }

    private fun setupButtons(view: View) {
        actionBar.setOnSearchClick {
            navController.navigate(R.id.action_exploreFragment_to_searchFragment)
        }
    }

    fun bindViews(view: View){
        actionBar = view.findViewById(R.id.action_bar)
    }
}