package ru.ikrom.fragment_bottom_menu

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.ikrom.base_adapter.AdapterItem

abstract class BaseBottomMenuViewModel<T: AdapterItem>: ViewModel() {

    protected val _headerState = MutableLiveData<T>()
    val headerState: LiveData<T> = _headerState

    abstract fun setupHeader(id: String, title: String, subtitle: String, thumbnail: String)
}