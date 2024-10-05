package ru.ikrom.menu_track

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import ru.ikrom.ui.base_adapter.CompositeAdapter
import ru.ikrom.ui.base_adapter.delegates.MenuButtonDelegate
import ru.ikrom.ui.base_adapter.delegates.MenuHeaderDelegate
import ru.ikrom.ui.base_adapter.delegates.MenuHeaderDelegateItem

@AndroidEntryPoint
class TracksMenu : BottomSheetDialogFragment(R.layout.fragment_bottom_sheet) {
    private lateinit var args: Args
    private lateinit var recyclerView: RecyclerView
    private lateinit var navController: NavController
    private var compositeAdapter = CompositeAdapter.Builder()
        .add(MenuHeaderDelegate({}))
        .add(MenuButtonDelegate({}))
        .build()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        navController = findNavController()
        args = Args(requireArguments())
        setupViews(view)
        setupRecyclerView()
        setupItems()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun setupViews(view: View) {
        recyclerView = view.findViewById(R.id.rv_content)
    }

    private fun setupRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = compositeAdapter
    }

    private fun setupItems() {
        compositeAdapter.addToStart(
            MenuHeaderDelegateItem(
                args.title,
                args.subtitle,
                args.thumbnail
            )
        )
    }

    inner class Args(bundle: Bundle) {
        val id: String = bundle.getString(ID) ?: ""
        val title: String = bundle.getString(TITLE) ?: ""
        val subtitle: String = bundle.getString(SUBTITLE) ?: ""
        val thumbnail: String = bundle.getString(THUMBNAIL) ?: ""
    }

    companion object {
        private const val TAG = "BottomMenuFragment"
        private const val ID = "id"
        private const val TITLE = "title"
        private const val SUBTITLE = "subtitle"
        private const val THUMBNAIL = "thumbnail"

        fun createBundle(
            id: String,
            title: String,
            subtitle: String,
            thumbnail: String
        ): Bundle {
            return bundleOf(
                ID to id,
                TITLE to title,
                SUBTITLE to subtitle,
                THUMBNAIL to thumbnail
            )
        }
    }
}