package xm.space.ultimatememspace.presentation.screens.profile

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import xm.space.ultimatememspace.R
import xm.space.ultimatememspace.business.domain.models.memes.MemesPack
import xm.space.ultimatememspace.business.repositories.avatars.AvatarRepository
import xm.space.ultimatememspace.business.repositories.shop.ShopRepository
import xm.space.ultimatememspace.core.models.toolbar.ToolbarState
import xm.space.ultimatememspace.core.viewmodel.BaseViewModel
import xm.space.ultimatememspace.navigation.ALL_MEMES_PACKS
import xm.space.ultimatememspace.navigation.CHOOSE_ROLE_CONTENT
import xm.space.ultimatememspace.navigation.PROFILE_CONTENT
import xm.space.ultimatememspace.presentation.screens.profile.models.ProfileState
import xm.space.ultimatememspace.presentation.screens.shop.models.ShopState

class ProfileViewModel(
    private val avatarRepository: AvatarRepository,
    private val shopRepository: ShopRepository
) : BaseViewModel() {

    val state: StateFlow<ProfileState>
        get() = _state
    private val _state = MutableStateFlow(ProfileState())

    init {
        viewModelScope.launch {
            _barState.value = ToolbarState(
                title = resourceProvider.getString(R.string.gen_profile),
                leftIcon = if (profileRepository.isProfileWasEdit()) R.drawable.ic_close else null,
                rightIcon = if (profileRepository.isProfileWasEdit()) R.drawable.ic_card else null
            )
            _state.value = ProfileState(
                currentNickname = profileRepository.getUserName(),
                nickPlaceholder = resourceProvider.getString(R.string.gen_name),
                playerAvatars = avatarRepository.getAvatars().map {
                    it.toUi(mineAvatarId = profileRepository.getUserAvatarId())
                },
                confirmTitle = resourceProvider.getString(R.string.gen_save),
                shopState = ShopState(
                    barTitle = resourceProvider.getString(R.string.gen_catalog),
                    memesOptions = if (profileRepository.isProfileWasEdit()) shopRepository.getShopPack()
                    else emptyList()
                )
            )
        }
    }

    fun onAvatarIconClick(id: Int) {
        _state.value = state.value.copy(
            playerAvatars = state.value.playerAvatars.map { avatar ->
                avatar.copy(isSelect = avatar.id == id)
            }
        )
    }

    fun onSaveClick(name: String, iconId: Int) = viewModelScope.launch {
        profileRepository.setPlayerData(
            id = name.hashCode(),
            name = name,
            avatarId = iconId
        ).run { navigateSomewhere() }
    }

    fun onBackEvent() = viewModelScope.launch {
        if (profileRepository.isProfileWasEdit()) {
            if (state.value.isCurtainShow) {
                onFullViewClose()
            } else {
                navigateSomewhere()
            }
        } else {
            settingsRepository.letFinishApp()
        }
    }

    fun onAllMemesAtPackClick(memesPack: MemesPack) = viewModelScope.launch {
        _state.value = state.value.copy(isCurtainShow = false)
        shopRepository.setActivePack(memesPack).run {
            navController?.navigate(ALL_MEMES_PACKS)
        }
    }

    fun onFullViewClose() {
        _state.value = state.value.copy(isCurtainShow = false)
    }

    fun onMemesShopClick() {
        _state.value = state.value.copy(isCurtainShow = true)
    }

    private fun navigateSomewhere() {
        navController?.navigate(CHOOSE_ROLE_CONTENT) {
            popUpTo(PROFILE_CONTENT) { inclusive = true }
        }
    }
}