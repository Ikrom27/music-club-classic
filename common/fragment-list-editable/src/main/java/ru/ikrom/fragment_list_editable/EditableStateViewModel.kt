package ru.ikrom.fragment_list_editable

import android.util.Log
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.ikrom.base_fragment.DefaultStateViewModel

sealed class EditableUiState<out T> {
    data class Success<out T>(val items: List<T>): EditableUiState<T>()
    data class Error(val e: Throwable): EditableUiState<Nothing>()
    data object Loading: EditableUiState<Nothing>()
    data object Empty: EditableUiState<Nothing>()
}

abstract class EditableStateViewModel<T>: DefaultStateViewModel<EditableUiState<T>>() {
    private var searchJob: Job? = null

    init {
        update()
    }

    fun update(filter: String = ""){
        searchJob?.cancel()
        searchJob = viewModelScope.launch(Dispatchers.IO) {
            delay(DEBOUNCE_PERIOD)
            updateItems(filter)
        }
    }

    private suspend fun updateItems(filter: String = "") {
        _state.postValue(EditableUiState.Loading)
        runCatching {
            val result = getData()
            if(filter.isNotBlank())
                result.filter { filterBy(it, filter) }
            else result
        }.onSuccess { result ->
            if(result.isNotEmpty()){
                _state.postValue(EditableUiState.Success(result))
            } else {
                _state.postValue(EditableUiState.Empty)
            }
        }.onFailure { e ->
            Log.e(TAG, e.message.toString())
            _state.postValue(EditableUiState.Error(e))
        }
    }

    protected fun updateState(data: List<T>){
        _state.postValue(EditableUiState.Success(data))
    }

    abstract suspend fun getData(): List<T>
    abstract fun filterBy(item: T, textQuery: String): Boolean

    override fun onCleared() {
        super.onCleared()
        searchJob?.cancel()
    }

    companion object {
        const val DEBOUNCE_PERIOD: Long = 500
        val TAG = EditableStateViewModel::class.simpleName
    }
}