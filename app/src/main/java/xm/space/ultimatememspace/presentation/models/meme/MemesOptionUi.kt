package xm.space.ultimatememspace.presentation.models.meme

import xm.space.ultimatememspace.business.domain.models.memes.MemesPack

data class MemesOptionUi(
    val optionIcon: Int,
    val optionTitle: String,
    val optionDesc: String,
    val freeAtPackCount: String,
    val paidAtPackCount: String,
    val optionPrice: String?,
    val memesPack: MemesPack
)