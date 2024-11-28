package xm.space.ultimatememspace.business.domain.models.memes

/**
 * Memes for player
 * @property playerId Identifier user
 * @property playerMemes Memes for user
 */
data class PlayerMemes(
    val playerId: Int,
    val playerMemes: String
)