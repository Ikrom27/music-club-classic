package com.ikrom.music_club_classic.ui.screens

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.activity.addCallback
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.textfield.TextInputEditText
import com.ikrom.music_club_classic.R
import com.ikrom.music_club_classic.data.model.Track
import com.ikrom.music_club_classic.playback.PlayerHandler
import com.ikrom.music_club_classic.ui.adapters.base_adapters.BaseAdapterCallBack
import com.ikrom.music_club_classic.ui.adapters.base_adapters.item_decorations.MarginItemDecoration
import com.ikrom.music_club_classic.ui.adapters.explore.SearchAdapter
import com.ikrom.music_club_classic.viewmodel.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SearchFragment : Fragment() {
    @Inject
    lateinit var playerHandler: PlayerHandler

    private lateinit var navController: NavController
    private lateinit var recyclerView: RecyclerView
    private lateinit var toolbar: MaterialToolbar
    private lateinit var searchField: TextInputEditText
    private lateinit var clearButton: ImageButton
    private lateinit var btnBack: ImageButton

    private val adapter = SearchAdapter(
        onItemClick =  { onItemClick(it) },
        onMoreButtonClick =  { onMoreButtonClick(it) })
    val viewModel: SearchViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_search, container, false)
        bindViews(view)
        setupAdapter()
        setupRecycleView()
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner){
            requireParentFragment().findNavController().navigateUp()
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        searchField.doOnTextChanged { text, start, before, count ->
            viewModel.updateSearchList(text.toString())
        }
        setupButtons()
    }

    fun bindViews(view: View){
        navController = requireParentFragment().findNavController()
        recyclerView = view.findViewById(R.id.rv_content)
        toolbar = view.findViewById(R.id.toolbar)
        searchField = view.findViewById(R.id.et_input_field)
        clearButton = view.findViewById(R.id.ib_clear)
        btnBack = view.findViewById(R.id.btn_back)
    }

    fun setupButtons(){
        clearButton.setOnClickListener {
            searchField.setText("")
        }
        btnBack.setOnClickListener{
            navController.navigateUp()
        }
    }


    fun setupAdapter(){
        viewModel.searchList.observe(viewLifecycleOwner){
            adapter.setItems(it)
        }
        adapter.attachCallBack(object : BaseAdapterCallBack<Track>(){
            override fun onItemClick(item: Track, view: View) {
                onItemClick(item)
            }
        })
    }

    fun onMoreButtonClick(track: Track){}

    fun onItemClick(track: Track){
        playerHandler.playNow(track)
    }

    fun setupRecycleView(){
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter
        val playerHeight = resources.getDimensionPixelSize(R.dimen.mini_player_height)
        val navbarHeight = resources.getDimensionPixelSize(R.dimen.bottom_nav_bar_height)
        val margin = resources.getDimensionPixelSize(R.dimen.medium_items_margin)
        recyclerView.addItemDecoration(
            MarginItemDecoration(
                startSpace = margin,
                endSpace = playerHeight + navbarHeight + margin,)
        )
    }

    companion object {
        private const val TAG = "SearchFragment"
    }
}