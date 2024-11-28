package xm.space.ultimatememspace.business.domain.models.memes

import xm.space.ultimatememspace.business.domain.models.memevote.MemeVote

/**
 * Data of chosen meme by player
 * @property userId User identifier
 * @property userMemId Identifier chosen meme
 */
data class MemeGameState(
    val userId: Int,
    val userMemId: Int
) {

    fun toMemeVote() = MemeVote(
        id = userId,
        userMemId = userMemId
    )
}