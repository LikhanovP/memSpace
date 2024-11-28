package xm.space.ultimatememspace.presentation.screens.servershutdown

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import xm.space.ultimatememspace.R
import xm.space.ultimatememspace.core.viewmodel.BaseViewModel
import xm.space.ultimatememspace.navigation.CHOOSE_ROLE_CONTENT
import xm.space.ultimatememspace.navigation.SERVER_SHUTDOWN
import xm.space.ultimatememspace.presentation.screens.servershutdown.models.ServerShutdownState

class ServerShutdownViewModel : BaseViewModel() {

    val state: StateFlow<ServerShutdownState>
        get() = _state
    private val _state = MutableStateFlow(ServerShutdownState(
        waitTitle = resourceProvider.getString(R.string.gen_server_down),
        proceedTitle = resourceProvider.getString(R.string.gen_its_clear)
    ))

    fun onProceedClick() {
        navController?.navigate(CHOOSE_ROLE_CONTENT) {
            popUpTo(SERVER_SHUTDOWN) { inclusive = true }
        }
    }
}