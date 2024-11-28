package xm.space.ultimatememspace.business.repositories.memes

import xm.space.ultimatememspace.business.domain.models.memes.MemeOption
import xm.space.ultimatememspace.business.domain.models.memes.MemesPack

/**
 * Data source of memes
 */
interface MemesRepository {

    /**
     * Getting all memes
     */
    suspend fun getMemes(): List<MemeOption>

    suspend fun getMemesAtPack(pack: MemesPack): List<MemeOption>

    fun getFreeMemesAtPack(pack: MemesPack): Int

    fun getPaidMemesAtPack(pack: MemesPack): Int
}