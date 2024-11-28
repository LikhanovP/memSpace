package xm.space.ultimatememspace.presentation.screens.relatedchoosebestmeme.choosebestmeme

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
import xm.space.ultimatememspace.business.domain.models.network.NetworkEvents.ResultVotesMeme
import xm.space.ultimatememspace.business.domain.models.network.NetworkEvents.PollingUsers
import xm.space.ultimatememspace.business.domain.models.network.NetworkEvents.BestMemChoosing
import xm.space.ultimatememspace.business.repositories.memes.MemesRepository
import xm.space.ultimatememspace.business.repositories.situations.SituationsRepository
import xm.space.ultimatememspace.core.models.toolbar.ToolbarState
import xm.space.ultimatememspace.core.uikit.components.image.models.CurtainState
import xm.space.ultimatememspace.core.viewmodel.BaseViewModel
import xm.space.ultimatememspace.core.viewmodel.GameProcessState
import xm.space.ultimatememspace.navigation.CHOOSE_BEST_MEME
import xm.space.ultimatememspace.navigation.ROUND_RESULT
import xm.space.ultimatememspace.navigation.WAIT_BEST_MEME
import xm.space.ultimatememspace.presentation.models.meme.MemeUi
import xm.space.ultimatememspace.presentation.models.meme.VoteBestMemeUi
import xm.space.ultimatememspace.presentation.screens.relatedchoosebestmeme.choosebestmeme.models.ChooseBestMemeState

/**
 * Screen logic for choosing best meme by situation
 */
class ChooseBestMemeViewModel(
    private val memesRepository: MemesRepository,
    private val situationsRepository: SituationsRepository
) : BaseViewModel() {

    val state: StateFlow<ChooseBestMemeState>
        get() = _state
    private val _state = MutableStateFlow(ChooseBestMemeState())

    init {
        viewModelScope.launch {
            _barState.value = ToolbarState(
                title = "${resourceProvider.getString(R.string.gen_round_order)} ${getRoundNumber()}"
            )
            _state.value = ChooseBestMemeState(
                memes = userPracticeManagerRepository.getMemesForBest().filter {
                    it.id != profileRepository.getUserId()
                }.map { memeVote ->
                    memeVote.toUi(
                        memeResourceId = memesRepository.getMemes().first {
                            it.memeId == memeVote.userMemId
                        }.memeResId
                    )
                },
                confirmVoteTitle = resourceProvider.getString(R.string.gen_choose),
                situationTitle = resourceProvider.getString(R.string.gen_situation),
                situationDescription = situationsRepository.getSituationAtRound()?.question.orEmpty(),
                memesTitle = resourceProvider.getString(R.string.gen_choose_best_meme),
                curtainState = CurtainState(
                    curtainCloseTitle = resourceProvider.getString(R.string.gen_close),
                    curtainAcceptTitle = resourceProvider.getString(R.string.gen_accept)
                )
            )

            eventsInteractor.observeEvents().collect { event ->
                when (event) {
                    is ClientDisconnectEvent -> {
                        _state.value = state.value.copy(isOverlayLoading = true)
                        onClientDisconnected(GameProcessState.ChooseBestMeme) {
                            _state.value = state.value.copy(isOverlayLoading = false)
                        }
                    }

                    is ServerShutdownEvent -> onServerShutdown()
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
                                )
                            ) {
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

    fun onBestMemSelectConfirm() = viewModelScope.launch {
        if (profileRepository.isProfileBelongAdmin()) {
            if (practiceManagerRepository.addPickPlayer(
                    playerWhoPickId = profileRepository.getUserId(),
                    memeId = state.value.memes.first { it.masterMeme.memeSelect }.masterMeme.memeId
                )
            ) {
                viewModelScope.launch {
                    networkRepository.memesVoteEvent()
                }
                navigateToResultRound()
            } else {
                navigateToWaitBestMeme()
            }
        } else {
            networkPlayerRepository.playerChooseBestMemEvent(
                playerWhoChooseId = profileRepository.getUserId(),
                memeId = state.value.memes.first { it.masterMeme.memeSelect }.masterMeme.memeId
            ).run {
                navigateToWaitBestMeme()
            }
        }
    }

    fun onBestMemWasSelect(memeId: Int) {
        _state.value = state.value.copy(
            memes = state.value.memes.map { meme ->
                if (meme.masterMeme.memeId == memeId) {
                    meme.copy(masterMeme = meme.masterMeme.copy(memeSelect = true))
                } else {
                    meme.copy(masterMeme = meme.masterMeme.copy(memeSelect = false))
                }
            }
        )
    }

    fun onBestMemeLongClick(resId: Int) {
        _state.value = state.value.copy(
            curtainState = state.value.curtainState.copy(
                fullViewResId = state.value.memes.indexOfFirst { it.masterMeme.memeResId == resId } to true
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

    fun onMemeCurtainSelect(resId: Int) {
        _state.value = state.value.copy(
            memes = state.value.memes.map { meme ->
                meme.copy(
                    masterMeme = meme.masterMeme.copy(
                        memeSelect = meme.masterMeme.memeResId == resId
                    )
                )
            },
            curtainState = state.value.curtainState.copy(
                fullViewResId = null to false
            )
        )
    }

    private fun getRoundNumber() = viewModelScope.launch {
        if (profileRepository.isProfileBelongAdmin())
            practiceManagerRepository.getRoundOrder().toString()
        else
            userPracticeManagerRepository.getRoundNumber().toString()
    }

    private fun navigateToWaitBestMeme() = navController?.navigate(WAIT_BEST_MEME) {
        popUpTo(CHOOSE_BEST_MEME) { inclusive = true }
    }

    private fun navigateToResultRound() = navController?.navigate(ROUND_RESULT) {
        popUpTo(CHOOSE_BEST_MEME) { inclusive = true }
    }
}