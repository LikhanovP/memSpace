package xm.space.ultimatememspace.presentation.models.player

import xm.space.ultimatememspace.presentation.models.avatar.AvatarUi

data class PlayerUi(
    val playerId: Int,
    val playerName: String,
    val playerIconId: Int,
    val playerIsMe: Boolean
) {

    fun getAvatarUi() = AvatarUi(
        id = playerId,
        resId = playerIconId,
        isSelect = false
    )
}