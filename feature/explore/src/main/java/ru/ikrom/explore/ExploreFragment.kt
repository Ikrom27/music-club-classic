package ru.ikrom.explore

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import ru.ikrom.appbar.AppBar
import ru.ikrom.ui.base_adapter.CompositeAdapter
import ru.ikrom.ui.base_adapter.delegates.CardAdapter
import ru.ikrom.ui.base_adapter.delegates.NestedItems
import ru.ikrom.ui.base_adapter.delegates.NestedItemsDelegate
import ru.ikrom.ui.base_adapter.delegates.TitleDelegate
import ru.ikrom.ui.base_adapter.delegates.TitleItem
import ru.ikrom.ui.base_adapter.delegates.CardItem
import ru.ikrom.ui.base_adapter.item_decorations.DecorationDimens
import ru.ikrom.ui.base_adapter.item_decorations.MarginItemDecoration
import javax.inject.Inject

@AndroidEntryPoint
class ExploreFragment : Fragment() {
    @Inject
    lateinit var navigator: Navigator

    private lateinit var appBar: AppBar
    private lateinit var navController: NavController
    private lateinit var recyclerView: RecyclerView
    private val viewModel: ExploreViewModel by viewModels()
    private val adapter = CompositeAdapter.Builder()
        .add(NestedItemsDelegate())
        .add(TitleDelegate())
        .build()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_explore, container, false)
        navController = requireParentFragment().findNavController()
        bindViews(view)
        setupRecyclerView()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.uiState.observe(viewLifecycleOwner) {state ->
            when(state){
                ExploreUiState.Error -> {}
                ExploreUiState.Loading -> {}
                is ExploreUiState.Success -> showContent(state.data)
            }
        }
    }

    private fun showContent(data: List<CardItem>){
        val newReleasesItem = NestedItems(
            data,
            CompositeAdapter.Builder()
                .add(CardAdapter(
                    onClick = {
                        navController.navigate(navigator.toAlbumScreenId(), bundleOf("id" to it.id))
                    },
                    onLongClick = {
                        navigator.toAlbumMenu(it.id)
                    }
                ))
                .build())
        adapter.updateItem(0, TitleItem("New releases"))
        adapter.updateItem(1, newReleasesItem)
    }

    private fun setupRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
        if (recyclerView.itemDecorationCount == 0){
            recyclerView.addItemDecoration(
                MarginItemDecoration(
                    endSpace = DecorationDimens.getBottomMargin(resources),
                    betweenSpace = DecorationDimens.getSectionMargin(resources)
                )
            )
        }
    }

    private fun bindViews(view: View){
        recyclerView = view.findViewById(R.id.rv_content)
        appBar = view.findViewById(R.id.action_bar)
        appBar.setOnSearchClick {
            navController.navigate(navigator.toSearchScreenId())
        }
    }

    interface Navigator {
        fun toSearchScreenId(): Int
        fun toAlbumScreenId(): Int
        fun toAlbumMenu(id: String)
    }
}