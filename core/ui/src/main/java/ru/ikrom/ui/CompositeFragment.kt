package ru.ikrom.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.ikrom.ui.base_adapter.CompositeAdapter

abstract class CompositeFragment : Fragment(R.layout.fragment_list) {
    abstract fun setupAdapter(): CompositeAdapter
    abstract fun setupData()

    protected lateinit var mRecyclerView: RecyclerView
    protected val compositeAdapter = setupAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mRecyclerView = view.findViewById(R.id.rv)
        mRecyclerView.layoutManager = LinearLayoutManager(context)
        mRecyclerView.adapter = compositeAdapter
        setupData()
    }


}