package ru.ikrom.explore

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import ru.ikrom.appbar.AppBar
import ru.ikrom.theme.AppDimens
import ru.ikrom.base_adapter.CompositeAdapter
import ru.ikrom.adapter_delegates.delegates.CardAdapter
import ru.ikrom.adapter_delegates.delegates.NestedItems
import ru.ikrom.adapter_delegates.delegates.NestedItemsDelegate
import ru.ikrom.adapter_delegates.delegates.TitleDelegate
import ru.ikrom.base_adapter.ThumbnailItem
import ru.ikrom.base_fragment.DefaultListFragment
import ru.ikrom.ui.base_adapter.item_decorations.MarginItemDecoration
import javax.inject.Inject

@AndroidEntryPoint
class ExploreFragment : DefaultListFragment<ExplorePageContent, ExploreViewModel>(R.layout.fragment_explore) {
    @Inject
    lateinit var navigator: Navigator
    override val mViewModel: ExploreViewModel by viewModels()
    private val compositeAdapter = CompositeAdapter.Builder()
        .add(TitleDelegate())
        .add(NestedItemsDelegate())
        .build()

    override fun getAdapter() = compositeAdapter
    override fun getRecyclerViewId() = R.id.rv_content
    override fun getLayoutManager() = LinearLayoutManager(context)

    override fun handleState(state: ExplorePageContent) {
        if(state.newReleases.isNotEmpty()){
            compositeAdapter.add(NestedItems(
                items = state.newReleases,
                adapter = CompositeAdapter.Builder()
                    .add(CardAdapter(
                        onClick = { navigator.toAlbumScreen(it.id) },
                        onLongClick = { navigator.toAlbumMenu(it) }
                )).build()))
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAppBar(view)
    }

    override fun recyclerViewConfigure(rv: RecyclerView) {
        super.recyclerViewConfigure(rv)
        if (rv.itemDecorationCount == 0){
            rv.addItemDecoration(
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
}