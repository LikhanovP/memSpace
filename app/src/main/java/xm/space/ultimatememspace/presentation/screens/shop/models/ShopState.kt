package xm.space.ultimatememspace.presentation.screens.shop.models

import xm.space.ultimatememspace.core.extensions.empty
import xm.space.ultimatememspace.presentation.models.meme.MemesOptionUi

data class ShopState(
    val barTitle: String = String.empty(),
    val memesOptions: List<MemesOptionUi> = emptyList()
)