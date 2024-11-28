package xm.space.ultimatememspace.presentation.screens.relatedchoosememe.choosememe

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
import xm.space.ultimatememspace.business.domain.models.network.NetworkEvents.PollingUsers
import xm.space.ultimatememspace.business.domain.models.network.NetworkEvents.VoteBestMem
import xm.space.ultimatememspace.business.domain.models.network.NetworkEvents.MemWasChoose
import xm.space.ultimatememspace.business.repositories.situations.SituationsRepository
import xm.space.ultimatememspace.core.models.toolbar.ToolbarState
import xm.space.ultimatememspace.core.uikit.components.image.models.CurtainState
import xm.space.ultimatememspace.core.viewmodel.BaseViewModel
import xm.space.ultimatememspace.core.viewmodel.GameProcessState
import xm.space.ultimatememspace.navigation.CHOOSE_BEST_MEME
import xm.space.ultimatememspace.navigation.CHOOSE_MEME
import xm.space.ultimatememspace.navigation.WAIT_MEME
import xm.space.ultimatememspace.presentation.screens.relatedchoosememe.choosememe.models.ChooseMemeState

/**
 * Screen logic by choose meme for situation
 */
class ChooseMemeViewModel(situationsRepository: SituationsRepository) : BaseViewModel() {

    val state: StateFlow<ChooseMemeState>
        get() = _state
    private val _state = MutableStateFlow(ChooseMemeState())

    init {
        viewModelScope.launch {
            _barState.value =
                ToolbarState(title = resourceProvider.getString(R.string.gen_choose_mem))
            situationsRepository.getSituationAtRound()?.let { situation ->
                _state.value = ChooseMemeState(
                    situation = situation.toUi(),
                    memes = userPracticeManagerRepository.getMemesPack(),
                    confirmMemeTitle = resourceProvider.getString(R.string.gen_choose),
                    memesTitle = resourceProvider.getString(R.string.gen_your_memes),
                    situationTitle = resourceProvider.getString(R.string.gen_situation),
                    curtainState = CurtainState(
                        curtainCloseTitle = resourceProvider.getString(R.string.gen_close),
                        curtainAcceptTitle = resourceProvider.getString(R.string.gen_accept)
                    )
                )
            }

            eventsInteractor.observeEvents().collect { event ->
                when (event) {
                    is ClientDisconnectEvent -> {
                        _state.value = state.value.copy(isOverlayLoading = true)
                        onClientDisconnected(GameProcessState.ChooseMeme) {
                            _state.value = state.value.copy(isOverlayLoading = false)
                        }
                    }

                    is ServerShutdownEvent -> onServerShutdown()
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

                    is ForServerEvent -> when (val value = event.event) {
                        is MemWasChoose -> {
                            if (
                                practiceManagerRepository.addUserMemeChoose(
                                    userId = value.userId,
                                    memeId = value.memId
                                )
                            ) {
                                viewModelScope.launch { networkRepository.choosesMemesEvent() }
                                navigateToChooseBestMeme()
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

    fun onMemeSelect(memeId: Int) {
        _state.value = state.value.copy(
            memes = state.value.memes.map { meme ->
                if (meme.memeId == memeId)
                    meme.copy(memeSelect = true)
                else
                    meme.copy(memeSelect = false)
            }
        )
    }

    fun onMemeConfirm() = viewModelScope.launch {
        val chosenMemeId = state.value.memes.find { it.memeSelect }?.memeId ?: 0
        userPracticeManagerRepository.removeMeme(chosenMemeId)
        if (profileRepository.isProfileBelongAdmin()) {
            if (
                practiceManagerRepository.addUserMemeChoose(
                    userId = profileRepository.getUserId(),
                    memeId = chosenMemeId
                )
            ) {
                viewModelScope.launch { networkRepository.choosesMemesEvent() }
                navigateToChooseBestMeme()
            } else navigateToWaitChooseMeme()
        } else {
            networkPlayerRepository.sendMemeEvent(memeId = chosenMemeId).run {
                navigateToWaitChooseMeme()
            }
        }
    }

    fun onFullViewImageClick(resId: Int) {
        _state.value = state.value.copy(
            curtainState = state.value.curtainState.copy(
                fullViewResId = state.value.memes.indexOfFirst { it.memeResId == resId } to true
            )
        )
    }

    fun onFullViewClose() {
        _state.value = state.value.copy(
            curtainState = state.value.curtainState.copy(
                fullViewResId = null to false
            )
        )
    }

    fun onMemeCurtainSelect(resId: Int) {
        _state.value = state.value.copy(
            memes = state.value.memes.map { meme ->
                meme.copy(memeSelect = meme.memeResId == resId)
            },
            curtainState = state.value.curtainState.copy(
                fullViewResId = null to false
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

    private fun navigateToWaitChooseMeme() = navController?.navigate(WAIT_MEME) {
        popUpTo(CHOOSE_MEME) { inclusive = true }
    }

    private fun navigateToChooseBestMeme() = navController?.navigate(CHOOSE_BEST_MEME) {
        popUpTo(CHOOSE_MEME) { inclusive = true }
    }
}