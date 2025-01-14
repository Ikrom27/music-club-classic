package ru.ikrom.base_fragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

abstract class DefaultStateViewModel<S>: ViewModel() {
    protected val _state = MutableLiveData<S>()
    val state: LiveData<S> = _state
}