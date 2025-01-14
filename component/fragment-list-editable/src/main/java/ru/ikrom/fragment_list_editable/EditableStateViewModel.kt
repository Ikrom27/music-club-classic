package ru.ikrom.fragment_list_editable

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.ikrom.base_fragment.DefaultStateViewModel

sealed class EditableUiState<out T> {
    data class Success<out T>(val items: List<T>): EditableUiState<T>()
    data class Error(val e: Throwable): EditableUiState<Nothing>()
    data object Loading: EditableUiState<Nothing>()
    data object Empty: EditableUiState<Nothing>()
}

abstract class EditableStateViewModel<T>: DefaultStateViewModel<EditableUiState<T>>() {
    override fun loadState() {
        _state.value = EditableUiState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                getData()
            }.onSuccess { result ->
                if(result.isNotEmpty()){
                    _state.postValue(EditableUiState.Success(result))
                } else {
                    _state.postValue(EditableUiState.Empty)
                }
            }.onFailure { e ->
                _state.value = EditableUiState.Error(e)
            }
        }
    }

    protected fun refresh(data: List<T>){
        _state.postValue(EditableUiState.Success(data))
    }

    abstract suspend fun getData(): List<T>
}