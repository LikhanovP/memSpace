package xm.space.ultimatememspace.business.domain.models.connecteduser

/**
 * User points data
 * @property userId User identifier
 * @property userPoints User points at game
 */
data class UserPoints(
    val userId: Int,
    val userPoints: List<Int> = emptyList()
)