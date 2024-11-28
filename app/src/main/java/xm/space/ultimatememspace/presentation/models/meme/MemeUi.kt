package xm.space.ultimatememspace.presentation.models.meme

/**
 * Data of meme
 * @property memeId Meme identifier
 * @property memeResId Meme resource identifier
 * @property memeSelect Select flag
 */
data class MemeUi(
    val memeId: Int,
    val memeResId: Int,
    val memeSelect: Boolean = false
)