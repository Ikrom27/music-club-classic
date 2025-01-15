package ru.ikrom.explore

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import ru.ikrom.appbar.AppBar
import ru.ikrom.theme.AppDimens
import ru.ikrom.ui.base_adapter.CompositeAdapter
import ru.ikrom.ui.base_adapter.delegates.CardAdapter
import ru.ikrom.ui.base_adapter.delegates.NestedItems
import ru.ikrom.ui.base_adapter.delegates.NestedItemsDelegate
import ru.ikrom.ui.base_adapter.delegates.TitleDelegate
import ru.ikrom.ui.base_adapter.delegates.TitleItem
import ru.ikrom.ui.base_adapter.delegates.CardItem
import ru.ikrom.ui.base_adapter.delegates.ThumbnailItem
import ru.ikrom.ui.base_adapter.item_decorations.MarginItemDecoration
import javax.inject.Inject

@AndroidEntryPoint
class ExploreFragment : Fragment(R.layout.fragment_explore) {
    @Inject
    lateinit var navigator: Navigator
    private lateinit var recyclerView: RecyclerView
    private val viewModel: ExploreViewModel by viewModels()
    private val adapter = CompositeAdapter.Builder()
        .add(NestedItemsDelegate())
        .add(TitleDelegate())
        .build()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAppBar(view)
        setupRecyclerView(view, savedInstanceState?.getInt(SCROLL_POSITION_KEY) ?: 0)
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
                        navigator.toAlbumScreen(it.id)
                    },
                    onLongClick = {
                        navigator.toAlbumMenu(it)
                    }
                ))
                .build())
        adapter.updateItemAt(0, TitleItem("New releases"))
        adapter.updateItemAt(1, newReleasesItem)
    }

    private fun setupRecyclerView(view: View, position: Int) {
        recyclerView = view.findViewById(R.id.rv_content)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
        recyclerView.scrollToPosition(position)
        if (recyclerView.itemDecorationCount == 0){
            recyclerView.addItemDecoration(
                MarginItemDecoration(
                    endSpace = AppDimens.HEIGHT_BOTTOM_NAVBAR,
                    betweenSpace = AppDimens.MARGIN_SECTIONS
                )
            )
        }
    }

    private fun setupAppBar(view: View){
        view.findViewById<AppBar>(R.id.action_bar).apply {
            setOnSearchClick {
                navigator.toSearchScreen()
            }
        }
    }

    interface Navigator {
        fun toSearchScreen()
        fun toAlbumScreen(albumId: String)
        fun toAlbumMenu(item: ThumbnailItem)
    }

    companion object {
        private val SCROLL_POSITION_KEY = "scroll_position_key"
    }
}