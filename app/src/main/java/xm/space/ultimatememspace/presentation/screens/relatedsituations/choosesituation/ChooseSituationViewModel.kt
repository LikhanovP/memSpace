package xm.space.ultimatememspace.presentation.screens.relatedsituations.choosesituation

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import xm.space.ultimatememspace.R
import xm.space.ultimatememspace.business.domain.models.events.EventsGameProcess.ForServerEvent
import xm.space.ultimatememspace.business.domain.models.events.EventsGameProcess.ServerShutdownEvent
import xm.space.ultimatememspace.business.domain.models.events.EventsGameProcess.ClientDisconnectEvent
import xm.space.ultimatememspace.business.domain.models.events.EventsGameProcess.ForClientEvent
import xm.space.ultimatememspace.business.domain.models.network.NetworkEvents.RemindUser
import xm.space.ultimatememspace.business.domain.models.network.NetworkEvents.ContinueGame
import xm.space.ultimatememspace.business.domain.models.network.NetworkEvents.PollingUsers
import xm.space.ultimatememspace.business.domain.models.network.NetworkEvents.QuestionWasChoose
import xm.space.ultimatememspace.business.repositories.situations.SituationsRepository
import xm.space.ultimatememspace.core.models.toolbar.ToolbarState
import xm.space.ultimatememspace.core.viewmodel.BaseViewModel
import xm.space.ultimatememspace.core.viewmodel.GameProcessState
import xm.space.ultimatememspace.navigation.CHOOSE_MEME
import xm.space.ultimatememspace.navigation.CHOOSE_SITUATION
import xm.space.ultimatememspace.presentation.screens.relatedsituations.choosesituation.models.ChooseSituationState

/**
 * Screen logic choosing situation at round
 */
class ChooseSituationViewModel(
    private val situationsRepository: SituationsRepository
) : BaseViewModel() {

    /**
     * Screen state
     */
    val state: StateFlow<ChooseSituationState>
        get() = _state
    private val _state = MutableStateFlow(ChooseSituationState())

    init {
        viewModelScope.launch {
            _barState.value = ToolbarState(title = resourceProvider.getString(R.string.gen_choose_situation))
            _state.value = state.value.copy(
                situations = userPracticeManagerRepository.getSituationsAtRound().map { it.toUi() },
                proceedTitle = resourceProvider.getString(R.string.gen_choose)
            )

            eventsInteractor.observeEvents().collect { event ->
                when (event) {
                    is ForClientEvent -> when (event.event) {
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
                        else -> Unit
                    }
                    is ForServerEvent -> when (event.event) {
                        is RemindUser -> viewModelScope.launch {
                            reconnectedManagerRepository.addOnlineUser(event.event.id)
                        }
                        else -> Unit
                    }
                    is ServerShutdownEvent -> onServerShutdown()
                    is ClientDisconnectEvent -> {
                        _state.value = state.value.copy(isOverlayLoading = true)
                        onClientDisconnected(
                            GameProcessState.ChooseSituation(practiceManagerRepository.getActivePlayerAtRound())
                        ) {
                            _state.value = state.value.copy(isOverlayLoading = false)
                        }
                    }
                }
            }
        }
    }

    fun onSituationConfirm() = viewModelScope.launch {
        val situationId = state.value.situations.first { it.isSelect }.id
        situationsRepository.saveSituationAtRound(situationId)
        if (profileRepository.isProfileBelongAdmin()) {
            navigateToChooseMeme()
        } else Unit
        viewModelScope.launch {
            networkRepository.choosesSituationEvent(situationId = situationId)
        }
    }

    fun onSituationSelect(id: Int) {
        _state.value = state.value.copy(
            situations = state.value.situations.map {
                if (id == it.id) it.copy(isSelect = true)
                else it.copy(isSelect = false)
            }
        )
    }

    private fun navigateToChooseMeme() {
        navController?.navigate(CHOOSE_MEME) {
            popUpTo(CHOOSE_SITUATION) { inclusive = true }
        }
    }
}