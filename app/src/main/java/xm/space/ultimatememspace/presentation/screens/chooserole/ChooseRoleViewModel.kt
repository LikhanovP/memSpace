package xm.space.ultimatememspace.presentation.screens.chooserole

import android.os.CountDownTimer
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import xm.space.ultimatememspace.R
import xm.space.ultimatememspace.business.domain.models.network.InvitationEvent
import xm.space.ultimatememspace.business.repositories.avatars.AvatarRepository
import xm.space.ultimatememspace.core.viewmodel.BaseViewModel
import xm.space.ultimatememspace.navigation.CHOOSE_ROLE_CONTENT
import xm.space.ultimatememspace.navigation.CONNECTED_PLAYERS
import xm.space.ultimatememspace.navigation.CREATING_GROUP
import xm.space.ultimatememspace.navigation.PROFILE_CONTENT
import xm.space.ultimatememspace.navigation.QR_SCANNER
import xm.space.ultimatememspace.presentation.screens.chooserole.models.ChooseRoleState

/**
 * Screen logic for choose role
 * @property resourceProvider Access to resource
 * @property profileRepository Data source for profile
 */
class ChooseRoleViewModel(
    avatarRepository: AvatarRepository
) : BaseViewModel() {

    /*** State for choose role */
    val state: StateFlow<ChooseRoleState>
        get() = _state
    private val _state = MutableStateFlow(ChooseRoleState())

    init {
        viewModelScope.launch {
            _state.value = ChooseRoleState(
                ownerDescription = resourceProvider.getString(R.string.gen_role_admin),
                userDescription = resourceProvider.getString(R.string.gen_role_user),
                profile = profileRepository.getFullProfile().toCardUi(avatarRepository.getAvatars())
            )
            startSearchServer()
        }
    }

    /*** Create party event */
    fun onCreatePartyClick() = viewModelScope.launch {
        profileRepository.setProfileRole(isAdmin = true).run {
            clearAllConnections()
            navController?.navigate(
                if (profileRepository.isProfileWasEdit()) {
                    CREATING_GROUP
                } else {
                    PROFILE_CONTENT
                }
            ) {
                popUpTo(CHOOSE_ROLE_CONTENT) { inclusive = true }
            }
        }
    }

    /*** User role event */
    fun onPlayerClick() = viewModelScope.launch {
        clearAllConnections()
        profileRepository.setProfileRole(isAdmin = false).run {
            navController?.navigate(
                if (profileRepository.isProfileWasEdit()) {
                    QR_SCANNER
                } else {
                    PROFILE_CONTENT
                }
            ) {
                popUpTo(CHOOSE_ROLE_CONTENT) { inclusive = true }
            }
        }
    }

    fun onProfileClick() = viewModelScope.launch {
        clearAllConnections()
        profileRepository.setStatusEditProfile().run {
            navController?.navigate(PROFILE_CONTENT) {
                popUpTo(CHOOSE_ROLE_CONTENT) { inclusive = true }
            }
        }
    }

    fun onConnectPartyClick() = viewModelScope.launch {
        settingsRepository.setNetworkData(
            ip = state.value.isThereAlreadyGameState?.ip.orEmpty(),
            port = state.value.isThereAlreadyGameState?.port.orEmpty()
        ).run {
            navController?.navigate(CONNECTED_PLAYERS) {
                popUpTo(CHOOSE_ROLE_CONTENT) {
                    inclusive = true
                }
            }
        }
    }

    private fun clearAllConnections() {
        invitationTimer.cancel()
        blindInvitationTimer.cancel()
        viewModelScope.launch {
            networkRepository.removeSearchServer()
        }
    }

    private val invitationTimer =
        object : CountDownTimer(VISIBLE_INVITATION_TIME, VISIBLE_INVITATION_TIME) {
            override fun onTick(timeTick: Long) { Unit }
            override fun onFinish() {
                _state.value = state.value.copy(
                    isThereAlreadyGameState = state.value.isThereAlreadyGameState?.copy(isActive = false)
                )
            }
        }

    private val blindInvitationTimer =
        object : CountDownTimer(BLIND_INVITATION_TIME, BLIND_INVITATION_TIME) {
            override fun onTick(timeTick: Long) { Unit }
            override fun onFinish() {
                viewModelScope.launch {
                    startSearchServer()
                }
            }
        }

    private suspend fun startSearchServer() {
        networkRepository.searchServer { value ->
            viewModelScope.launch {
                blindInvitationTimer.start()
                _state.value = state.value.copy(
                    isThereAlreadyGameState = value.copy(
                        ip = value.ip,
                        port = value.port,
                        isActive = true,
                        titleValue = "${value.ownerName} ${resourceProvider.getString(R.string.gen_invitation_game)}"
                    )
                )
                invitationTimer.start()
            }
        }
    }

    companion object {

        private const val VISIBLE_INVITATION_TIME = 5000L

        private const val BLIND_INVITATION_TIME = 10000L
    }
}