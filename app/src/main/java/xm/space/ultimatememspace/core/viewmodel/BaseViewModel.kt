package xm.space.ultimatememspace.core.viewmodel

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.google.gson.annotations.Until
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import xm.space.ultimatememspace.R
import xm.space.ultimatememspace.business.domain.interactors.EventsInteractor
import xm.space.ultimatememspace.business.repositories.network.NetworkRepository
import xm.space.ultimatememspace.business.repositories.networkplayer.NetworkPlayerRepository
import xm.space.ultimatememspace.business.repositories.practicemanager.PracticeManagerRepository
import xm.space.ultimatememspace.business.repositories.practicemanager.UserPracticeManagerRepository
import xm.space.ultimatememspace.business.repositories.profile.ProfileRepository
import xm.space.ultimatememspace.business.repositories.reconnectmanager.ReconnectedManagerRepository
import xm.space.ultimatememspace.business.repositories.settings.SettingsRepository
import xm.space.ultimatememspace.core.models.toolbar.ToolbarState
import xm.space.ultimatememspace.core.providers.ResourceProvider
import xm.space.ultimatememspace.navigation.CHOOSE_BEST_MEME
import xm.space.ultimatememspace.navigation.CHOOSE_SITUATION
import xm.space.ultimatememspace.navigation.SERVER_SHUTDOWN
import xm.space.ultimatememspace.core.viewmodel.GameProcessState.Room
import xm.space.ultimatememspace.core.viewmodel.GameProcessState.ChooseBestMeme
import xm.space.ultimatememspace.core.viewmodel.GameProcessState.ChooseMeme
import xm.space.ultimatememspace.core.viewmodel.GameProcessState.ChooseSituation
import xm.space.ultimatememspace.core.viewmodel.GameProcessState.WaitChooseSituation
import xm.space.ultimatememspace.core.viewmodel.GameProcessState.WaitChooseBestMeme
import xm.space.ultimatememspace.core.viewmodel.GameProcessState.WaitChooseMeme
import xm.space.ultimatememspace.core.viewmodel.GameProcessState.GameViewResults
import xm.space.ultimatememspace.core.viewmodel.GameProcessState.Empty
import xm.space.ultimatememspace.core.viewmodel.GameProcessState.ViewResults
import xm.space.ultimatememspace.navigation.ALL_BAD

/**
 * Basic viewModel
 */
abstract class BaseViewModel : ViewModel(), KoinComponent {

    val barState: StateFlow<ToolbarState>
        get() = _barState
    val _barState = MutableStateFlow(ToolbarState())

    private var doubleBackToExitPressedOnce = false

    var navController: NavController? = null

    val context: Context by inject()

    val resourceProvider: ResourceProvider by inject()

    val eventsInteractor: EventsInteractor by inject()

    val networkRepository: NetworkRepository by inject()

    val profileRepository: ProfileRepository by inject()

    val networkPlayerRepository: NetworkPlayerRepository by inject()

    val reconnectedManagerRepository: ReconnectedManagerRepository by inject()

    val practiceManagerRepository: PracticeManagerRepository by inject()

    val userPracticeManagerRepository: UserPracticeManagerRepository by inject()

    val settingsRepository: SettingsRepository by inject()

    fun onServerShutdown() {
        viewModelScope.launch {
            networkRepository.closeServer().run {
                navController?.navigate(SERVER_SHUTDOWN) {
                    //navController?.graph?.clear()
                }
            }
        }
    }

    fun onClientDisconnected(
        gameProcessState: GameProcessState,
        restoreGameCallback: (Boolean) -> Unit
    ) {
        viewModelScope.launch {
            reconnectedManagerRepository.addOnlineUser(profileRepository.getUserId())
            networkRepository.pollingUsers()
            reconnectedManagerRepository.startOnlineCheck { onlineUsers ->
                viewModelScope.launch {
                    reconnectedManagerRepository.changeLockValue(true)
                    practiceManagerRepository.recheckOnlineUsers(onlineUsers).run {
                        restoreGameState(gameProcessState) { isJustRestore ->
                            restoreGameCallback.invoke(isJustRestore)
                        }
                    }
                }
            }
        }
    }

    fun getOverlayTitle(resId: Int): String {
        return resourceProvider.getString(resId)
    }

    fun closeAllConnections() = viewModelScope.launch {
        networkRepository.closeServer()
        networkPlayerRepository.closeSocket()
    }

    fun onBackFinishClick() {
        if (doubleBackToExitPressedOnce) {
            closeAllConnections()
            viewModelScope.launch {
                settingsRepository.letFinishApp()
            }
            return
        }
        doubleBackToExitPressedOnce = true
        Toast.makeText(context, resourceProvider.getString(R.string.gen_please_back), Toast.LENGTH_SHORT).show()
        Handler(Looper.getMainLooper()).postDelayed(
            { doubleBackToExitPressedOnce = false },
            2000
        )
    }

    private fun restoreGameState(
        gameProcessState: GameProcessState,
        restoreGameCallback: (Boolean) -> Unit
    ) {
        viewModelScope.launch {
            if (practiceManagerRepository.isPlayersEmpty()) {
                if (gameProcessState is Room) {
                    restoreGame(status = true, callback = restoreGameCallback)
                } else {
                    navController?.navigate(ALL_BAD) {
                        popUpTo(gameProcessState.getKey()) { inclusive = true }
                    }
                }
            } else {
                when (gameProcessState) {
                    is ChooseSituation -> restoreGame(status = true, callback = restoreGameCallback)
                    is WaitChooseSituation -> {
                        if (practiceManagerRepository.isActiveUserInParty(gameProcessState.activePlayerId)) {
                            restoreGame(status = true, callback = restoreGameCallback)
                        } else {
                            practiceManagerRepository.getNextUserAfterRecheck()?.let { player ->
                                networkRepository.inActionPlayerEvent(
                                    player = player,
                                    questions = practiceManagerRepository.getSituationsAtGame()
                                ).run {
                                    if (player.id == profileRepository.getUserId()) {
                                        navController?.navigate(CHOOSE_SITUATION) {
                                            popUpTo(gameProcessState.getKey()) { inclusive = true }
                                        }
                                    } else {
                                        restoreGameCallback.invoke(true)
                                    }
                                }
                            } ?: restoreGameCallback.invoke(false)
                        }
                    }

                    is GameViewResults -> restoreGame(status = true, callback = restoreGameCallback)
                    is ViewResults -> restoreGame(status = true, callback = restoreGameCallback)
                    is ChooseMeme -> restoreGame(status = true, callback = restoreGameCallback)
                    is WaitChooseMeme -> {
                        if (practiceManagerRepository.allMemePicksDone()) {
                            restoreGame(status = true, callback = restoreGameCallback)
                        } else {
                            restoreGame(status = false, callback = restoreGameCallback)
                        }
                    }

                    is ChooseBestMeme -> restoreGame(status = true, callback = restoreGameCallback)
                    is WaitChooseBestMeme -> {
                        if (practiceManagerRepository.allBestMemePicksDone()) {
                            restoreGameCallback.invoke(false)
                        } else {
                            restoreGame(status = true, callback = restoreGameCallback)
                        }
                    }

                    is Empty -> Unit
                    is Room -> restoreGame(status = true, callback = restoreGameCallback)
                    else -> Unit
                }
            }
        }
    }

    private fun restoreGame(status: Boolean, callback: (Boolean) -> Unit) = viewModelScope.launch {
        networkRepository.continueGame().run { callback.invoke(status) }
    }
}