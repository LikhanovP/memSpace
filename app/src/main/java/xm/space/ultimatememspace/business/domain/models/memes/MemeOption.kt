package xm.space.ultimatememspace.business.domain.models.memes

import xm.space.ultimatememspace.presentation.models.meme.MemeUi

/**
 * Data of meme
 * @property memeId Identifier of meme
 * @property memeResId Identifier image of meme
 */
data class MemeOption(
    val memeId: Int,
    val memeResId: Int,
    val isFree: Boolean = true
) {

    /*** Transfer [MemeOption] to [MemeUi] */
    fun toUi() = MemeUi(
        memeId = memeId,
        memeResId = memeResId
    )
}