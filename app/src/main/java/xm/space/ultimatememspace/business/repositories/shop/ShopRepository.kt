package xm.space.ultimatememspace.business.repositories.shop

import xm.space.ultimatememspace.business.domain.models.memes.MemesPack
import xm.space.ultimatememspace.presentation.models.meme.MemesOptionUi

interface ShopRepository {

    suspend fun getShopPack(): List<MemesOptionUi>

    suspend fun setActivePack(memesPack: MemesPack)

    suspend fun getActivePack(): List<Int>

    fun getActivePackName(): String
}