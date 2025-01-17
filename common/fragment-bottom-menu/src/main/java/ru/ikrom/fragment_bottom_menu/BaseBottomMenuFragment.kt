package ru.ikrom.fragment_bottom_menu

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ru.ikrom.base_adapter.AdapterItem
import ru.ikrom.base_adapter.CompositeAdapter
import ru.ikrom.base_adapter.ThumbnailArgs

abstract class BaseBottomMenuFragment<VM: BaseBottomMenuViewModel<out AdapterItem>> : BottomSheetDialogFragment(R.layout.fragment_bottom_sheet) {
    abstract val mAdapter: CompositeAdapter
    abstract val mViewModel: BaseBottomMenuViewModel<out AdapterItem>

    private val args: ThumbnailArgs by lazy { ThumbnailArgs(requireArguments()) }

    abstract fun getMenuButtons(): List<AdapterItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel.setupHeader(args.id, args.title, args.subtitle, args.thumbnail)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView(view)
        mViewModel.headerState.observe(viewLifecycleOwner) { header ->
            mAdapter.setItems(
                listOf(header) + getMenuButtons()
            )
        }
    }

    private fun setupRecyclerView(view: View) {
        view.findViewById<RecyclerView>(R.id.rv_content).apply {
            layoutManager = LinearLayoutManager(context)
            adapter = mAdapter
        }
    }
}