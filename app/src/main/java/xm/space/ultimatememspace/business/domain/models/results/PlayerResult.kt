package xm.space.ultimatememspace.business.domain.models.results

/**
 * Result for player
 * @property playerName User nickname
 * @property playerAvatar User avatar identifier
 * @property playerWinsCount number of wins in a round
 * @property playerIsWinner Winner flag
 */
data class PlayerResult(
    val playerName: String,
    val playerAvatar: Int,
    val playerWinsCount: Int,
    val playerIsWinner: Boolean
)