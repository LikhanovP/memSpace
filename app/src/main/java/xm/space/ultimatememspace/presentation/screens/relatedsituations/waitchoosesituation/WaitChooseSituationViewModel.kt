package xm.space.ultimatememspace.presentation.screens.relatedsituations.waitchoosesituation

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import xm.space.ultimatememspace.R
import xm.space.ultimatememspace.business.domain.models.events.EventsGameProcess.ForServerEvent
import xm.space.ultimatememspace.business.domain.models.events.EventsGameProcess.ForClientEvent
import xm.space.ultimatememspace.business.domain.models.events.EventsGameProcess.ServerShutdownEvent
import xm.space.ultimatememspace.business.domain.models.events.EventsGameProcess.ClientDisconnectEvent
import xm.space.ultimatememspace.business.domain.models.network.NetworkEvents.RemindUser
import xm.space.ultimatememspace.business.domain.models.network.NetworkEvents.ContinueGame
import xm.space.ultimatememspace.business.domain.models.network.NetworkEvents.PollingUsers
import xm.space.ultimatememspace.business.domain.models.network.NetworkEvents.QuestionWasChoose
import xm.space.ultimatememspace.business.domain.models.network.NetworkEvents.StartGame
import xm.space.ultimatememspace.business.repositories.situations.SituationsRepository
import xm.space.ultimatememspace.core.viewmodel.BaseViewModel
import xm.space.ultimatememspace.core.viewmodel.GameProcessState
import xm.space.ultimatememspace.navigation.CHOOSE_MEME
import xm.space.ultimatememspace.navigation.CHOOSE_SITUATION
import xm.space.ultimatememspace.navigation.GAME_RESULT
import xm.space.ultimatememspace.navigation.WAIT_SITUATION
import xm.space.ultimatememspace.presentation.screens.relatedsituations.waitchoosesituation.models.WaitChooseState

/**
 * Loader screen logic
 * @property resourceProvider Access to resource
 */
class WaitChooseSituationViewModel(
    private val situationsRepository: SituationsRepository
) : BaseViewModel() {

    val state: StateFlow<WaitChooseState>
        get() = _state
    private val _state = MutableStateFlow(
        WaitChooseState(waitTitle = resourceProvider.getString(R.string.gen_wait_user_choose))
    )

    init {
        viewModelScope.launch {
            eventsInteractor.observeEvents().collect { event ->
                when (event) {
                    is ClientDisconnectEvent -> {
                        _state.value = state.value.copy(isOverlayLoading = true)
                        onClientDisconnected(
                            GameProcessState.WaitChooseSituation(
                                practiceManagerRepository.getActivePlayerAtRound()
                            )
                        ) {
                            if (it) {
                                _state.value = state.value.copy(isOverlayLoading = false)
                            } else {
                                navController?.navigate(GAME_RESULT) {
                                    popUpTo(WAIT_SITUATION) { inclusive = true }
                                }
                            }
                        }
                    }
                    is ServerShutdownEvent -> onServerShutdown()
                    is ForClientEvent -> when(event.event) {
                        is QuestionWasChoose -> {
                            situationsRepository.saveSituationAtRound(event.event.id).run {
                                navigateToChooseMeme()
                            }
                        }
                        is ContinueGame -> _state.value = state.value.copy(isOverlayLoading = false)
                        is PollingUsers -> {
                            _state.value = state.value.copy(isOverlayLoading = true)
                            networkPlayerRepository.expressYourself()
                        }
                        is StartGame -> userPracticeManagerRepository.saveSituationsAtRound(
                            situations = event.event.questions
                        ).run {
                            navController?.navigate(
                                if (event.event.mainUserId == profileRepository.getUserId()) {
                                    CHOOSE_SITUATION
                                } else {
                                    WAIT_SITUATION
                                }
                            ) {
                                popUpTo(WAIT_SITUATION) { inclusive = true }
                            }
                        }
                        else -> Unit
                    }
                    is ForServerEvent -> when(event.event) {
                        is QuestionWasChoose -> {
                            situationsRepository.saveSituationAtRound(event.event.id).run {
                                networkRepository.choosesSituationEvent(
                                    situationId = event.event.id
                                ).also { navigateToChooseMeme() }
                            }
                        }
                        is RemindUser -> viewModelScope.launch {
                            reconnectedManagerRepository.addOnlineUser(event.event.id)
                        }
                        else -> Unit
                    }
                }
            }
        }
    }

    private fun navigateToChooseMeme() {
        navController?.navigate(CHOOSE_MEME) {
            popUpTo(CHOOSE_SITUATION) { inclusive = true }
        }
    }
}