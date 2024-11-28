package xm.space.ultimatememspace.presentation.models.result

/**
 * Data of votes for meme
 * @property memeId Meme identifier
 * @property memeResId Meme resource id
 * @property votes Number of votes
 * @property isWinner Flag of win
 * @property playerName User name
 * @property playerIcon User icon id
 */
data class ResultUi(
    val memeId: Int,
    val memeResId: Int,
    val votes: Int,
    val isWinner: Boolean = false,
    val playerName: String,
    val playerIcon: Int
)