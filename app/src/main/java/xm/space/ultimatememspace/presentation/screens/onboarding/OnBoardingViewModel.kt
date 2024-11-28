package xm.space.ultimatememspace.presentation.screens.onboarding

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import xm.space.ultimatememspace.R
import xm.space.ultimatememspace.business.domain.models.onboarding.toUi
import xm.space.ultimatememspace.business.repositories.onboarding.OnBoardingRepository
import xm.space.ultimatememspace.business.repositories.settings.SettingsRepository
import xm.space.ultimatememspace.core.models.toolbar.ToolbarState
import xm.space.ultimatememspace.core.viewmodel.BaseViewModel
import xm.space.ultimatememspace.presentation.screens.onboarding.models.OnBoardingItem

/**
 * Screen logic with initial instructions
 * @property onBoardingRepository Data source for onBoarding
 * @property resourceProvider Access to resource
 * @property settingsRepository Data source for app
 */
class OnBoardingViewModel(
    private val onBoardingRepository: OnBoardingRepository
) : BaseViewModel() {

    val state: StateFlow<List<OnBoardingItem>>
        get() = _state
    private val _state = MutableStateFlow<List<OnBoardingItem>>(emptyList())

    init {
        _barState.value = ToolbarState(
            title = resourceProvider.getString(R.string.app_name),
            leftIcon = R.drawable.ic_close
        )

        viewModelScope.launch {
            _state.value =
                onBoardingRepository.getOnBoardingOptions().map { it.toUi(resourceProvider) }
        }
    }

    /*** Proceed action title */
    val proceedTitle = resourceProvider.getString(R.string.gen_next)

    /*** Remember onboarding was viewed */
    fun onboardingAnnounced() = viewModelScope.launch {
        settingsRepository.setOnboardingViewed()
    }
}