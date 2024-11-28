package xm.space.ultimatememspace.presentation.screens.roundresult

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import xm.space.ultimatememspace.R
import xm.space.ultimatememspace.business.domain.models.events.EventsGameProcess.ForServerEvent
import xm.space.ultimatememspace.business.domain.models.events.EventsGameProcess.ForClientEvent
import xm.space.ultimatememspace.business.domain.models.events.EventsGameProcess.ClientDisconnectEvent
import xm.space.ultimatememspace.business.domain.models.events.EventsGameProcess.ServerShutdownEvent
import xm.space.ultimatememspace.business.domain.models.network.NetworkEvents.RemindUser
import xm.space.ultimatememspace.business.domain.models.network.NetworkEvents.ContinueGame
import xm.space.ultimatememspace.business.domain.models.network.NetworkEvents.PlayersNewMemes
import xm.space.ultimatememspace.business.domain.models.network.NetworkEvents.StartGame
import xm.space.ultimatememspace.business.domain.models.network.NetworkEvents.PollingUsers
import xm.space.ultimatememspace.business.domain.models.network.NetworkEvents.ViewGameResult
import xm.space.ultimatememspace.core.models.toolbar.ToolbarState
import xm.space.ultimatememspace.core.uikit.components.image.models.CurtainState
import xm.space.ultimatememspace.core.viewmodel.BaseViewModel
import xm.space.ultimatememspace.core.viewmodel.GameProcessState
import xm.space.ultimatememspace.navigation.CHOOSE_SITUATION
import xm.space.ultimatememspace.navigation.CONNECTED_PLAYERS
import xm.space.ultimatememspace.navigation.GAME_RESULT
import xm.space.ultimatememspace.navigation.ROUND_RESULT
import xm.space.ultimatememspace.navigation.WAIT_SITUATION
import xm.space.ultimatememspace.presentation.screens.roundresult.models.RoundResultState

/**
 * Screen logic for view round result
 */
class RoundResultViewModel : BaseViewModel() {

    val state: StateFlow<RoundResultState>
        get() = _state
    private val _state = MutableStateFlow(RoundResultState())

    init {
        viewModelScope.launch {
            val (winners, others) = if (profileRepository.isProfileBelongAdmin())
                practiceManagerRepository.getResultRound()
            else
                userPracticeManagerRepository.getResultRound()

            _barState.value = ToolbarState(title = resourceProvider.getString(R.string.gen_results))

            _state.value = RoundResultState(
                winnersMemes = winners,
                otherMemes = others,
                isOwner = profileRepository.isProfileBelongAdmin(),
                nextRoundTitle = resourceProvider.getString(R.string.gen_continue_game),
                winnerTitle = resourceProvider.getString(
                    if (winners.size > 1) R.string.gen_winners else R.string.gen_winner
                ),
                curtainState = CurtainState(
                    curtainCloseTitle = resourceProvider.getString(R.string.gen_close)
                )
            )

            eventsInteractor.observeEvents().collect { event ->
                when (event) {
                    is ServerShutdownEvent -> onServerShutdown()
                    is ClientDisconnectEvent -> {
                        _state.value = state.value.copy(isOverlayLoading = true)
                        onClientDisconnected(GameProcessState.ViewResults) {
                            _state.value = state.value.copy(isOverlayLoading = false)
                        }
                    }

                    is ForServerEvent -> when (event.event) {
                        is RemindUser -> viewModelScope.launch {
                            reconnectedManagerRepository.addOnlineUser(event.event.id)
                        }

                        else -> Unit
                    }

                    is ForClientEvent -> when (val value = event.event) {
                        is PlayersNewMemes -> {
                            userPracticeManagerRepository.addNewMemeToPack(
                                memes = value.playersMemes.first {
                                    it.playerId == profileRepository.getUserId()
                                }.playerMemes
                            )
                        }

                        is ContinueGame -> _state.value =
                            state.value.copy(isOverlayLoading = false)

                        is PollingUsers -> {
                            _state.value = state.value.copy(isOverlayLoading = true)
                            networkPlayerRepository.expressYourself()
                        }

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

                        is ViewGameResult -> {
                            userPracticeManagerRepository.saveGameResult(value.playersResults)
                                .run {
                                    navigateToGameResult()
                                }
                        }

                        else -> Unit
                    }
                }
            }
        }
    }

    fun onNextRoundClick() = viewModelScope.launch {
        if (practiceManagerRepository.isTheEnd()) {
            practiceManagerRepository.calculateResult().toSet().toList().let { results ->
                networkRepository.gameResultEvent(playersResults = results).run {
                    userPracticeManagerRepository.saveGameResult(results).run {
                        navigateToGameResult()
                    }
                }
            }
        } else {
            practiceManagerRepository.goNextRound()?.let { currentUser ->
                viewModelScope.launch {
                    val newMemes = practiceManagerRepository.getNewPlayersMemes()
                    userPracticeManagerRepository.addNewMemeToPack(
                        newMemes.first { playerMemes ->
                            playerMemes.playerId == profileRepository.getUserId()
                        }.playerMemes
                    ).run {
                        networkRepository.newMemesForEachPlayer(newMemes).run {
                            networkRepository.inActionPlayerEvent(
                                player = currentUser,
                                questions = practiceManagerRepository.getSituationsAtGame()
                            ).also {
                                practiceManagerRepository.updateRoundOrder()
                                navController?.navigate(
                                    if (currentUser.id == profileRepository.getUserId()) {
                                        CHOOSE_SITUATION
                                    } else WAIT_SITUATION
                                ) {
                                    popUpTo(ROUND_RESULT) { inclusive = true }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    fun onFullViewClose() {
        _state.value = state.value.copy(
            curtainState = state.value.curtainState.copy(fullViewResId = null to false)
        )
    }

    fun onFullViewImageClick(resId: Int) {
        _state.value = state.value.copy(
            curtainState = state.value.curtainState.copy(
                fullViewResId = state.value.winnersMemes.toMutableList().apply {
                    addAll(state.value.otherMemes)
                }.indexOfFirst { it.memeResId == resId } to true
            )
        )
    }

    fun onGeneralBackClick() {
        if (state.value.curtainState.fullViewResId.second) {
            _state.value = state.value.copy(
                curtainState = state.value.curtainState.copy(
                    fullViewResId = null to false
                )
            )
        } else {
            onBackFinishClick()
        }
    }

    private fun navigateToGameResult() {
        navController?.navigate(GAME_RESULT) {
            popUpTo(ROUND_RESULT) { inclusive = true }
        }
    }
}