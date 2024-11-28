package xm.space.ultimatememspace.presentation.screens.allbad

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import xm.space.ultimatememspace.R
import xm.space.ultimatememspace.core.models.toolbar.ToolbarState
import xm.space.ultimatememspace.core.viewmodel.BaseViewModel
import xm.space.ultimatememspace.navigation.ALL_BAD
import xm.space.ultimatememspace.navigation.CHOOSE_ROLE_CONTENT
import xm.space.ultimatememspace.presentation.screens.allbad.models.AllBadState

class AllBadViewModel : BaseViewModel() {

    val state: StateFlow<AllBadState>
        get() = _state
    private val _state = MutableStateFlow(
        AllBadState(
            proceedTitle = resourceProvider.getString(R.string.gen_its_clear),
            errorTitle = resourceProvider.getString(R.string.get_all_bad),
            errorDescription = resourceProvider.getString(R.string.get_try_again)
        )
    )

    fun onProceedClick() {
        navController?.navigate(
            CHOOSE_ROLE_CONTENT
        ) {
            popUpTo(ALL_BAD) { inclusive = true }
        }
    }
}