package xm.space.ultimatememspace.presentation.screens.gameresult

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import xm.space.ultimatememspace.R
import xm.space.ultimatememspace.business.domain.models.events.EventsGameProcess.ForServerEvent
import xm.space.ultimatememspace.business.domain.models.events.EventsGameProcess.ForClientEvent
import xm.space.ultimatememspace.business.domain.models.events.EventsGameProcess.ClientDisconnectEvent
import xm.space.ultimatememspace.business.domain.models.events.EventsGameProcess.ServerShutdownEvent
import xm.space.ultimatememspace.business.domain.models.network.NetworkEvents
import xm.space.ultimatememspace.business.domain.models.network.NetworkEvents.ContinueGame
import xm.space.ultimatememspace.business.domain.models.network.NetworkEvents.PollingUsers
import xm.space.ultimatememspace.core.models.toolbar.ToolbarState
import xm.space.ultimatememspace.core.viewmodel.BaseViewModel
import xm.space.ultimatememspace.core.viewmodel.GameProcessState
import xm.space.ultimatememspace.navigation.CHOOSE_SITUATION
import xm.space.ultimatememspace.navigation.CREATING_GROUP
import xm.space.ultimatememspace.navigation.WAIT_SITUATION
import xm.space.ultimatememspace.presentation.screens.gameresult.models.GameResultState

/**
 * Screen logic for view game result
 */
class GameResultViewModel : BaseViewModel() {

    val state: StateFlow<GameResultState>
        get() = _state
    private val _state = MutableStateFlow(GameResultState())

    init {
        viewModelScope.launch {
            val (winners, others) = userPracticeManagerRepository.getGameResults()
            _barState.value =
                ToolbarState(title = resourceProvider.getString(R.string.gen_game_results))
            _state.value = GameResultState(
                winnerTitle = resourceProvider.getString(
                    if (winners.size > 1) R.string.gen_winners else R.string.gen_winner
                ),
                winnerGame = winners,
                isOwner = profileRepository.isProfileBelongAdmin(),
                resultsByPlayers = others,
                nextRoundTitle = resourceProvider.getString(R.string.gen_game_retry)
            )
        }

        viewModelScope.launch {
            eventsInteractor.observeEvents().collect { event ->
                when (event) {
                    is ServerShutdownEvent -> onServerShutdown()
                    is ClientDisconnectEvent -> {
                        _state.value = state.value.copy(isOverlayLoading = true)
                        onClientDisconnected(GameProcessState.GameViewResults) {
                            _state.value = state.value.copy(isOverlayLoading = false)
                        }
                    }

                    is ForClientEvent -> when (event.event) {
                        is ContinueGame -> _state.value = state.value.copy(isOverlayLoading = false)
                        is PollingUsers -> {
                            _state.value = state.value.copy(isOverlayLoading = true)
                            networkPlayerRepository.expressYourself()
                        }

                        else -> Unit
                    }

                    is ForServerEvent -> {
                        when (val event = event.event) {
                            is NetworkEvents.RemindUser -> viewModelScope.launch {
                                reconnectedManagerRepository.addOnlineUser(event.id)
                            }

                            else -> Unit
                        }
                    }
                }
            }
        }
    }

    fun onNewGameClick() = viewModelScope.launch {
        practiceManagerRepository.clearAllDataBeforeNewGame().run {
            practiceManagerRepository.initializeGameState().run {
                userPracticeManagerRepository.saveMemesStartPack(
                    memes = this.first {
                        it.playerId == profileRepository.getUserId()
                    }.playerMemes
                ).let {
                    networkRepository.memesForEachPlayer(this@run).run {
                        practiceManagerRepository.setQueueUsers()?.let { activePlayer ->
                            val situations = practiceManagerRepository.getSituationsAtGame()
                            networkRepository.inActionPlayerEvent(
                                player = activePlayer,
                                questions = situations
                            ).also {
                                practiceManagerRepository.updateRoundOrder()
                                userPracticeManagerRepository.saveSituationsAtRound(
                                    situations = situations
                                ).run {
                                    navController?.navigate(
                                        if (activePlayer.id == profileRepository.getUserId()) {
                                            CHOOSE_SITUATION
                                        } else {
                                            WAIT_SITUATION
                                        }
                                    ) {
                                        popUpTo(CREATING_GROUP) {
                                            inclusive = true
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}