package xm.space.ultimatememspace.presentation.screens.connectedplayers

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import xm.space.ultimatememspace.R
import xm.space.ultimatememspace.business.domain.models.events.EventsGameProcess.ForClientEvent
import xm.space.ultimatememspace.business.domain.models.events.EventsGameProcess.ServerShutdownEvent
import xm.space.ultimatememspace.business.domain.models.events.EventsGameProcess.ClientDisconnectEvent
import xm.space.ultimatememspace.business.domain.models.network.NetworkEvents.ContinueGame
import xm.space.ultimatememspace.business.domain.models.network.NetworkEvents.StartGame
import xm.space.ultimatememspace.business.domain.models.network.NetworkEvents.LineUp
import xm.space.ultimatememspace.business.domain.models.network.NetworkEvents.PlayersMemes
import xm.space.ultimatememspace.business.domain.models.network.NetworkEvents.PollingUsers
import xm.space.ultimatememspace.business.repositories.avatars.AvatarRepository
import xm.space.ultimatememspace.business.repositories.models.ClientSocketEvents.ClientOpenEvent
import xm.space.ultimatememspace.core.models.toolbar.ToolbarState
import xm.space.ultimatememspace.core.viewmodel.BaseViewModel
import xm.space.ultimatememspace.presentation.screens.connectedplayers.models.ConnectedPlayersState
import xm.space.ultimatememspace.core.viewmodel.GameProcessState
import xm.space.ultimatememspace.navigation.CHOOSE_SITUATION
import xm.space.ultimatememspace.navigation.CONNECTED_PLAYERS
import xm.space.ultimatememspace.navigation.WAIT_SITUATION

class ConnectedPlayersViewModel(
    private val avatarRepository: AvatarRepository
) : BaseViewModel() {

    private val _state = MutableStateFlow(ConnectedPlayersState())
    val state: StateFlow<ConnectedPlayersState>
        get() = _state

    init {
        observeEvents().run { startGame() }
        viewModelScope.launch {
            _barState.value = ToolbarState(title = resourceProvider.getString(R.string.gen_players))
            networkPlayerRepository.observeSocketEvents().collect {
                when (it) {
                    is ClientOpenEvent -> networkPlayerRepository.userProfileEvent()
                    else -> Unit
                }
            }
        }
    }

    private fun startGame() {
        viewModelScope.launch {
            networkPlayerRepository.createSocket()
        }
    }

    private fun observeEvents() {
        viewModelScope.launch {
            eventsInteractor.observeEvents().collect { event ->
                when (event) {
                    is ForClientEvent -> when (val value = event.event) {
                        is ContinueGame -> _state.value = state.value.copy(isOverlayLoading = false)
                        is PollingUsers -> {
                            _state.value = state.value.copy(isOverlayLoading = true)
                            networkPlayerRepository.expressYourself()
                        }
                        is LineUp -> {
                            _state.value = state.value.copy(
                                players = value.connUsers.map {
                                    it.toUi(
                                        mineId = profileRepository.getUserId(),
                                        avatars = avatarRepository.getAvatars()
                                    )
                                }
                            )
                        }
                        is PlayersMemes -> userPracticeManagerRepository.saveMemesStartPack(
                            memes = value.playersMemes.first {
                                it.playerId == profileRepository.getUserId()
                            }.playerMemes
                        )
                        is StartGame -> userPracticeManagerRepository.saveSituationsAtRound(
                            situations = value.questions
                        ).run {
                            navController?.navigate(
                                if (value.mainUserId == profileRepository.getUserId()) {
                                    CHOOSE_SITUATION
                                } else {
                                    WAIT_SITUATION
                                }
                            ) {
                                popUpTo(CONNECTED_PLAYERS) { inclusive = true }
                            }
                        }
                        else -> Unit
                    }
                    is ClientDisconnectEvent -> {
                        _state.value = state.value.copy(isOverlayLoading = true)
                        onClientDisconnected(GameProcessState.Room) {
                            _state.value = state.value.copy(isOverlayLoading = false)
                        }
                    }
                    is ServerShutdownEvent -> onServerShutdown()
                    else -> Unit
                }
            }
        }
    }
}