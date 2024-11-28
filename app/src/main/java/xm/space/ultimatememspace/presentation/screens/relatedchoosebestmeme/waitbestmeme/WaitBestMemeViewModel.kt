package xm.space.ultimatememspace.presentation.screens.relatedchoosebestmeme.waitbestmeme

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import xm.space.ultimatememspace.R
import xm.space.ultimatememspace.business.domain.models.events.EventsGameProcess.ForClientEvent
import xm.space.ultimatememspace.business.domain.models.events.EventsGameProcess.ForServerEvent
import xm.space.ultimatememspace.business.domain.models.events.EventsGameProcess.ClientDisconnectEvent
import xm.space.ultimatememspace.business.domain.models.events.EventsGameProcess.ServerShutdownEvent
import xm.space.ultimatememspace.business.domain.models.network.NetworkEvents.RemindUser
import xm.space.ultimatememspace.business.domain.models.network.NetworkEvents.ContinueGame
import xm.space.ultimatememspace.business.domain.models.network.NetworkEvents.PollingUsers
import xm.space.ultimatememspace.business.domain.models.network.NetworkEvents.ResultVotesMeme
import xm.space.ultimatememspace.business.domain.models.network.NetworkEvents.BestMemChoosing
import xm.space.ultimatememspace.business.repositories.practicemanager.UserPracticeManagerRepository
import xm.space.ultimatememspace.core.extensions.empty
import xm.space.ultimatememspace.core.models.toolbar.ToolbarState
import xm.space.ultimatememspace.core.viewmodel.BaseViewModel
import xm.space.ultimatememspace.core.viewmodel.GameProcessState
import xm.space.ultimatememspace.navigation.ROUND_RESULT
import xm.space.ultimatememspace.navigation.WAIT_BEST_MEME
import xm.space.ultimatememspace.presentation.screens.relatedchoosebestmeme.waitbestmeme.models.WaitBestMemeState

/**
 * Screen logic for waiting best meme from other users
 */
class WaitBestMemeViewModel(
    userPracticeManagerRepository: UserPracticeManagerRepository
) : BaseViewModel() {

    val state: StateFlow<WaitBestMemeState>
        get() = _state
    private val _state = MutableStateFlow(
        WaitBestMemeState(
            waitTitle = resourceProvider.getString(R.string.gen_wait_choose_mem)
        )
    )

    init {
        viewModelScope.launch {
            _barState.value = ToolbarState(title = String.empty())
            eventsInteractor.observeEvents().collect { event ->
                when (event) {
                    is ServerShutdownEvent -> onServerShutdown()
                    is ClientDisconnectEvent -> {
                        _state.value = state.value.copy(isOverlayLoading = true)
                        onClientDisconnected(GameProcessState.WaitChooseBestMeme) { isJustRestore ->
                            viewModelScope.launch {
                                if (isJustRestore) {
                                    _state.value = state.value.copy(isOverlayLoading = false)
                                } else {
                                    networkRepository.memesVoteEvent().run {
                                        navigateToResultRound()
                                    }
                                }
                            }
                        }
                    }
                    is ForClientEvent -> when (val value = event.event) {
                        is ResultVotesMeme -> {
                            userPracticeManagerRepository.saveResultRound(value.votes).run {
                                navigateToResultRound()
                            }
                        }
                        is ContinueGame -> _state.value = state.value.copy(isOverlayLoading = false)
                        is PollingUsers -> {
                            _state.value = state.value.copy(isOverlayLoading = true)
                            networkPlayerRepository.expressYourself()
                        }
                        else -> Unit
                    }
                    is ForServerEvent -> when (val value = event.event) {
                        is BestMemChoosing -> {
                            if (practiceManagerRepository.addPickPlayer(
                                playerWhoPickId = value.playerWhoChooseId,
                                memeId = value.memeId
                            )) {
                                networkRepository.memesVoteEvent().run {
                                    navigateToResultRound()
                                }
                            }
                        }
                        is RemindUser -> viewModelScope.launch {
                            reconnectedManagerRepository.addOnlineUser(value.id)
                        }
                        else -> Unit
                    }
                }
            }
        }
    }

    private fun navigateToResultRound() = navController?.navigate(ROUND_RESULT) {
        popUpTo(WAIT_BEST_MEME) { inclusive = true }
    }
}