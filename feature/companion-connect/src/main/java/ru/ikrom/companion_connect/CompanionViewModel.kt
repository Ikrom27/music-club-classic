package ru.ikrom.companion_connect

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.ikrom.adapter_delegates.delegates.MenuButtonItem
import ru.ikrom.companion_repository.CompanionRepository
import ru.ikrom.player_handler.IPlayerHandler
import javax.inject.Inject

@HiltViewModel
class CompanionViewModel @Inject constructor(
    private val repository: CompanionRepository,
    private val playerHandler: IPlayerHandler
) : ViewModel() {
    private var _companionData = MutableLiveData<List<MenuButtonItem>>()
    val companionData: LiveData<List<MenuButtonItem>> = _companionData

    init {
        fetchData()
    }

    private fun fetchData(){
        viewModelScope.launch(Dispatchers.IO) {
            _companionData.postValue(
                repository.getSavedCompanionData().map { companion ->
                    val (ip, port) = companion.data.split(":")
                    MenuButtonItem(
                        title = companion.data,
                        icon = null,
                        onClick = {
                            playerHandler.connectToCompanion(ip, port.toInt())
                        }
                    )
                }
            )
        }
    }
}