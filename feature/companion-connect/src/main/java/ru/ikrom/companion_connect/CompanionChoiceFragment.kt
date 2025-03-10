package ru.ikrom.companion_connect

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import ru.ikrom.adapter_delegates.delegates.MenuButtonDelegate
import ru.ikrom.adapter_delegates.delegates.MenuButtonItem
import ru.ikrom.base_adapter.CompositeAdapter
import ru.ikrom.fragment_bottom_menu.R
import ru.ikrom.theme.AppDrawableIds
import javax.inject.Inject

@AndroidEntryPoint
class CompanionChoiceFragment : BottomSheetDialogFragment(R.layout.fragment_bottom_sheet) {
    private val viewModel: CompanionViewModel by viewModels()
    private val mAdapter = CompositeAdapter.Builder()
        .add(MenuButtonDelegate(
            onClick = {
                it.onClick()
                dismiss()
            }
        ))
        .build()
    @Inject
    lateinit var navigator: Navigator

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView(view)
        viewModel.companionData.observe(viewLifecycleOwner) {
            mAdapter.setItems(it)
            mAdapter.add(MenuButtonItem(
                icon = AppDrawableIds.QR_CODE,
                title = "Scann QR code",
                onClick = {
                    navigator.toScan()
                }
            ))
        }
    }

    private fun setupRecyclerView(view: View) {
        view.findViewById<RecyclerView>(R.id.rv_content).apply {
            layoutManager = LinearLayoutManager(context)
            adapter = mAdapter
        }
    }

    interface Navigator {
        fun toScan()
    }
}