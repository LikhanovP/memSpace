package xm.space.ultimatememspace.presentation.screens.someerror

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import xm.space.ultimatememspace.core.extensions.empty
import xm.space.ultimatememspace.core.models.toolbar.ToolbarState
import xm.space.ultimatememspace.core.viewmodel.BaseViewModel
import xm.space.ultimatememspace.navigation.CHOOSE_ROLE_CONTENT
import xm.space.ultimatememspace.presentation.screens.someerror.models.SomeErrorState

/**
 * Screen logic for some errors
 */
class SomeErrorViewModel : BaseViewModel() {

    val state: StateFlow<SomeErrorState>
        get() = _state
    private val _state = MutableStateFlow(SomeErrorState())

    init {
        _barState.value = ToolbarState(title = String.empty())
    }

    fun onProceedClick() {
        navController?.navigate(CHOOSE_ROLE_CONTENT) {
            navController?.graph?.clear()
        }
    }
}