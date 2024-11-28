package xm.space.ultimatememspace.business.domain.models.connecteduser

import xm.space.ultimatememspace.business.domain.models.avatar.Avatar
import xm.space.ultimatememspace.presentation.models.player.PlayerUi
import xm.space.ultimatememspace.presentation.models.profile.ProfileCardUi

/**
 * User data for online player
 * @property id Identifier player
 * @property name Player name
 * @property avatar Players avatar id
 */
data class ConnectedUser(
    val id: Int,
    val name: String,
    val avatar: Int
) {

    /*** Transform [ConnectedUser] to [PlayerUi] */
    fun toUi(mineId: Int, avatars: List<Avatar>) = PlayerUi(
        playerId = id,
        playerName = name,
        playerIconId = avatars.find { it.iconId == avatar }?.iconResId ?: 0,
        playerIsMe = mineId == id
    )

    fun toCardUi(avatars: List<Avatar>) = ProfileCardUi(
        id = id,
        playerName = name,
        profileAvatar = avatars.find { it.iconId == avatar }?.iconResId ?: 0
    )
}