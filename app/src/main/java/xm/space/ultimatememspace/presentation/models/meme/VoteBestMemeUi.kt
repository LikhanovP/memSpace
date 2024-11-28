package xm.space.ultimatememspace.presentation.models.meme

/**
 * Data of vote best meme
 * @property masterPlayerId Identifier owner meme
 * @property masterMeme Data of meme
 */
data class VoteBestMemeUi(
    val masterPlayerId: Int,
    val masterMeme: MemeUi
)