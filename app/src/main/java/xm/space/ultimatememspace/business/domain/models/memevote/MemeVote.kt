package xm.space.ultimatememspace.business.domain.models.memevote

import xm.space.ultimatememspace.presentation.models.meme.MemeUi
import xm.space.ultimatememspace.presentation.models.meme.VoteBestMemeUi

//TODO()
data class MemeVote(
    val id: Int,
    val userMemId: Int
) {

    fun toUi(memeResourceId: Int) = VoteBestMemeUi(
        masterPlayerId = id,
        masterMeme = MemeUi(
            memeId = userMemId,
            memeResId = memeResourceId
        )
    )
}