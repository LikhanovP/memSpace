package xm.space.ultimatememspace.presentation.screens.allmemespacks

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import xm.space.ultimatememspace.R
import xm.space.ultimatememspace.business.repositories.shop.ShopRepository
import xm.space.ultimatememspace.core.models.toolbar.ToolbarState
import xm.space.ultimatememspace.core.uikit.components.image.models.CurtainState
import xm.space.ultimatememspace.core.viewmodel.BaseViewModel
import xm.space.ultimatememspace.presentation.screens.allmemespacks.models.AllMemesPacksState

class AllMemesPacksViewModel(
    shopRepository: ShopRepository
) : BaseViewModel() {

    val state: StateFlow<AllMemesPacksState>
        get() = _state
    private val _state = MutableStateFlow(AllMemesPacksState(emptyList()))

    init {
        viewModelScope.launch {
            _barState.value = ToolbarState(
                title = shopRepository.getActivePackName(),
                leftIcon = R.drawable.ic_close
            )
            _state.value = AllMemesPacksState(
                memesOptionsResIds = shopRepository.getActivePack(),
                curtainState = CurtainState(
                    curtainCloseTitle = resourceProvider.getString(R.string.gen_close),
                    dotSpace = 2,
                    dotWidth = 4
                )
            )
        }
    }

    fun onCloseClick() {
        navController?.popBackStack()
    }

    fun onMemeViewerClick(memeResId: Int) {
        _state.value = state.value.copy(
            curtainState = state.value.curtainState.copy(
                fullViewResId = state.value.memesOptionsResIds.indexOfFirst { it == memeResId } to true
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
            navController?.popBackStack()
        }
    }
}