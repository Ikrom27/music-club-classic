package ru.ikrom.companion_connect.scanner

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.ikrom.companion_repository.CompanionRepository
import ru.ikrom.player_handler.IPlayerHandler
import javax.inject.Inject

@HiltViewModel
class ScannerViewModel @Inject constructor(
    private val repository: CompanionRepository,
    private val playerHandler: IPlayerHandler
) : ViewModel() {

    fun saveCompanionData(data: String) {
        viewModelScope.launch() {
            repository.saveCompanionData(data)
        }
    }

    fun connectTo(ip: String, port: Int) {
        playerHandler.connectToCompanion(ip, port)
    }
}