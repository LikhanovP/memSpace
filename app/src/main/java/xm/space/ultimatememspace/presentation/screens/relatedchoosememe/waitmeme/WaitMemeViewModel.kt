package xm.space.ultimatememspace.presentation.screens.relatedchoosememe.waitmeme

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import xm.space.ultimatememspace.core.viewmodel.BaseViewModel
import xm.space.ultimatememspace.presentation.screens.relatedchoosememe.waitmeme.models.WaitMemeState
import xm.space.ultimatememspace.business.domain.models.events.EventsGameProcess.ForServerEvent
import xm.space.ultimatememspace.business.domain.models.events.EventsGameProcess.ForClientEvent
import xm.space.ultimatememspace.business.domain.models.events.EventsGameProcess.ServerShutdownEvent
import xm.space.ultimatememspace.business.domain.models.events.EventsGameProcess.ClientDisconnectEvent
import xm.space.ultimatememspace.business.domain.models.network.NetworkEvents.RemindUser
import xm.space.ultimatememspace.business.domain.models.network.NetworkEvents.ContinueGame
import xm.space.ultimatememspace.business.domain.models.network.NetworkEvents.VoteBestMem
import xm.space.ultimatememspace.business.domain.models.network.NetworkEvents.PollingUsers
import xm.space.ultimatememspace.business.domain.models.network.NetworkEvents.MemWasChoose
import xm.space.ultimatememspace.core.viewmodel.GameProcessState
import xm.space.ultimatememspace.navigation.CHOOSE_BEST_MEME
import xm.space.ultimatememspace.navigation.WAIT_MEME

/**
 * Screen logic by waiting choose meme at round
 * @property practiceManagerRepository
 * @property userPracticeManagerRepository
 */
class WaitMemeViewModel : BaseViewModel() {

    /*** Screen state */
    val state: StateFlow<WaitMemeState>
        get() = _state
    private val _state = MutableStateFlow(WaitMemeState())

    init {
        viewModelScope.launch {
            eventsInteractor.observeEvents().collect { event ->
                when (event) {
                    is ForServerEvent -> when (event.event) {
                        is MemWasChoose -> {
                            if (
                                practiceManagerRepository.addUserMemeChoose(
                                    memeId = event.event.memId,
                                    userId = event.event.userId
                                )
                            ) {
                                networkRepository.choosesMemesEvent().run {
                                    navigateToChooseBestMeme()
                                }
                            }
                        }
                        is RemindUser -> viewModelScope.launch {
                            reconnectedManagerRepository.addOnlineUser(event.event.id)
                        }
                        else -> Unit
                    }
                    is ForClientEvent -> when (event.event) {
                        is VoteBestMem -> {
                            userPracticeManagerRepository.saveMemesForBest(
                                memes = event.event.memes,
                                roundNumber = event.event.roundOrder
                            ).run {
                                navigateToChooseBestMeme()
                            }
                        }
                        is ContinueGame -> _state.value = state.value.copy(isOverlayLoading = false)
                        is PollingUsers -> {
                            _state.value = state.value.copy(isOverlayLoading = true)
                            networkPlayerRepository.expressYourself()
                        }
                        else -> Unit
                    }
                    is ServerShutdownEvent -> onServerShutdown()
                    is ClientDisconnectEvent -> {
                        _state.value = state.value.copy(isOverlayLoading = true)
                        onClientDisconnected(GameProcessState.WaitChooseMeme) { isContinueGame ->
                            if (isContinueGame) {
                                viewModelScope.launch {
                                    networkRepository.choosesMemesEvent().run {
                                        navigateToChooseBestMeme()
                                    }
                                }
                            } else {
                                _state.value = state.value.copy(isOverlayLoading = false)
                            }
                        }
                    }
                }
            }
        }
    }

    /*** Route to choose best meme */
    private fun navigateToChooseBestMeme() {
        navController?.navigate(CHOOSE_BEST_MEME) {
            popUpTo(WAIT_MEME) { inclusive = true }
        }
    }
}