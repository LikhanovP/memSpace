package xm.space.ultimatememspace.business.domain.models.avatar

import xm.space.ultimatememspace.presentation.models.avatar.AvatarUi

/**
 * Avatar data
 * @property iconId Identifier icon
 * @property iconResId Identifier res id
 */
data class Avatar(
    val iconId: Int,
    val iconResId: Int
) {

    /*** Transform [Avatar] to [AvatarUi] */
    fun toUi(mineAvatarId: Int) = AvatarUi(
        id = iconId,
        resId = iconResId,
        isSelect = mineAvatarId == iconId
    )
}