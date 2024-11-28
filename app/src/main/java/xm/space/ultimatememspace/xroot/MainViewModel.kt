package xm.space.ultimatememspace.xroot

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import xm.space.ultimatememspace.business.repositories.network.NetworkRepository
import xm.space.ultimatememspace.business.repositories.networkplayer.NetworkPlayerRepository
import xm.space.ultimatememspace.business.repositories.profile.ProfileRepository
import xm.space.ultimatememspace.business.repositories.settings.SettingsRepository

/**
 * Initial activity logic
 * @property settingsRepository Settings app
 */
class MainViewModel(
    private val settingsRepository: SettingsRepository,
    private val networkRepository: NetworkRepository,
    private val networkPlayerRepository: NetworkPlayerRepository,
    private val profileRepository: ProfileRepository
) : ViewModel() {

    /*** Onboarding state */
    val isOnBoardingWasShowed: StateFlow<Pair<Boolean?, Boolean?>>
        get() = _isOnBoardingWasShowed
    private val _isOnBoardingWasShowed: MutableStateFlow<Pair<Boolean?, Boolean?>> =
        MutableStateFlow(null to null)

    val finishState: StateFlow<Boolean?>
        get() = _finishState
    private val _finishState: MutableStateFlow<Boolean?> = MutableStateFlow(null)

    init {
        viewModelScope.launch {
            _isOnBoardingWasShowed.value = settingsRepository.isOnBoardingViewed() to profileRepository.isProfileWasEdit()
            settingsRepository.observeFinishState().collect {
                _finishState.value = it
            }
        }
    }

    override fun onCleared() {
        closeAllConnections()
        super.onCleared()
    }

    fun closeAllConnections() = viewModelScope.launch {
        networkRepository.closeServer()
        networkPlayerRepository.closeSocket()
    }
}