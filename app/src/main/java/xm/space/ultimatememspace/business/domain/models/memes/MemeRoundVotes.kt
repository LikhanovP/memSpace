package xm.space.ultimatememspace.business.domain.models.memes

import xm.space.ultimatememspace.presentation.models.result.ResultUi

data class MemeRoundVotes(
    val memeId: Int,
    val count: Int,
    val playerName: String,
    val playerIcon: Int
) {
    /**
     * Transform [MemeRoundVotes] в [ResultUi]
     * @param memeResId Meme resource identifier
     * @param isWinner Flag of win
     */
    fun toUi(
        memeResId: Int,
        isWinner: Boolean,
        avatarResId: Int
    ) = ResultUi(
        memeId = memeId,
        votes = count,
        memeResId = memeResId,
        isWinner = isWinner,
        playerName = playerName,
        playerIcon = avatarResId
    )
}