package xm.space.ultimatememspace.business.domain.models.memes

/**
 * Data of chosen meme at round
 * @property playerWhoPickId Identifier player
 * @property memePickId Identifier meme
 */
data class PlayerPickMeme(
    val playerWhoPickId: Int,
    val memePickId: Int
)