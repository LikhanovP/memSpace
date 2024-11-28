package xm.space.ultimatememspace.presentation.screens.profile.models

import xm.space.ultimatememspace.core.extensions.empty
import xm.space.ultimatememspace.presentation.models.avatar.AvatarUi
import xm.space.ultimatememspace.presentation.screens.shop.models.ShopState

data class ProfileState(
    val currentNickname: String = String.empty(),
    val nickPlaceholder: String = String.empty(),
    val playerAvatars: List<AvatarUi> = emptyList(),
    val confirmTitle: String = String.empty(),
    val shopState: ShopState = ShopState(),
    val isCurtainShow: Boolean = false
)